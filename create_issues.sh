#!/usr/bin/env bash
#
# create_issues.sh — bulk-create GitHub issues from issues.tsv using gh CLI
#
# Prerequisites:
#   1. Install gh CLI: https://cli.github.com/
#   2. Authenticate: gh auth login
#   3. Run this script from inside your project's git repo (it must already
#      have a GitHub remote), OR pass the repo explicitly with -R owner/repo
#
# Usage:
#   ./create_issues.sh                       # uses current repo
#   ./create_issues.sh -R yourname/job-tracker  # explicit repo
#
# What it does:
#   - Reads issues.tsv (week<TAB>type<TAB>title<TAB>body)
#   - Ensures week labels (week-1 .. week-12) and type labels exist
#   - Creates one GitHub issue per row with both labels applied
#   - Safe to re-run: gh will just create duplicates if you do, so don't
#     run it twice without clearing issues first. Consider --dry-run.

set -euo pipefail

REPO_FLAG=""
DRY_RUN=false

while [[ $# -gt 0 ]]; do
  case "$1" in
    -R)
      REPO_FLAG="-R $2"
      shift 2
      ;;
    --dry-run)
      DRY_RUN=true
      shift
      ;;
    *)
      echo "Unknown argument: $1"
      exit 1
      ;;
  esac
done

if ! command -v gh &> /dev/null; then
  echo "Error: gh CLI not found. Install from https://cli.github.com/"
  exit 1
fi

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
TSV_FILE="$SCRIPT_DIR/issues.tsv"

if [[ ! -f "$TSV_FILE" ]]; then
  echo "Error: issues.tsv not found at $TSV_FILE"
  exit 1
fi

# Distinct week labels found in the file (W1 -> week-1, etc.)
WEEK_LABELS=$(grep -v '^#' "$TSV_FILE" | cut -f1 | sort -u | sed 's/^W/week-/')
TYPE_LABELS=$(grep -v '^#' "$TSV_FILE" | cut -f2 | sort -u)

echo "==> Ensuring labels exist..."
for label in $WEEK_LABELS; do
  if $DRY_RUN; then
    echo "[dry-run] would ensure label: $label"
  else
    gh label create "$label" $REPO_FLAG --color "BFD4F2" --force 2>/dev/null || true
  fi
done

declare -A TYPE_COLORS=( [backend]="D93F0B" [infra]="0E8A16" [frontend]="1D76DB" [testing]="FBCA04" [docs]="5319E7" )
for label in $TYPE_LABELS; do
  color="${TYPE_COLORS[$label]:-CCCCCC}"
  if $DRY_RUN; then
    echo "[dry-run] would ensure label: $label"
  else
    gh label create "$label" $REPO_FLAG --color "$color" --force 2>/dev/null || true
  fi
done

echo "==> Creating issues..."
count=0
while IFS=$'\t' read -r week type title body; do
  [[ "$week" =~ ^#.*$ || -z "$week" ]] && continue
  week_label="week-${week#W}"

  if $DRY_RUN; then
    echo "[dry-run] would create: [$week_label/$type] $title"
  else
    gh issue create $REPO_FLAG \
      --title "$title" \
      --body "$body" \
      --label "$week_label" \
      --label "$type" \
      > /dev/null
    echo "Created: [$week_label/$type] $title"
  fi
  count=$((count + 1))
done < "$TSV_FILE"

echo "==> Done. $count issues processed."
echo "View your board: gh issue list $REPO_FLAG"
echo "Or create a Project board: gh project create --owner @me --title 'Job Tracker Build'"
