#!/usr/bin/env bash
# Compares the last test run (target/php-src-failures.txt) with known-failures.txt.
# Usage: ./compare-failures.sh [--update]   (--update: accept the current run as the new baseline, only if nothing regressed)
set -euo pipefail

cd "$(dirname "$0")"
KNOWN=src/test/resources/consulo/php/phpsrc/known-failures.txt
ACTUAL=target/php-src-failures.txt

regressed=$(comm -13 <(grep -v '^#' "$KNOWN" | sort) <(grep -v '^#' "$ACTUAL" | sort))
fixed=$(comm -23 <(grep -v '^#' "$KNOWN" | sort) <(grep -v '^#' "$ACTUAL" | sort))

echo "known: $(grep -vc '^#' "$KNOWN")  actual: $(grep -vc '^#' "$ACTUAL")  fixed: $(printf '%s' "$fixed" | grep -c . || true)  regressed: $(printf '%s' "$regressed" | grep -c . || true)"
if [ -n "$regressed" ]; then
    echo "--- regressed:"
    echo "$regressed"
fi

if [ "${1:-}" = "--update" ]; then
    if [ -n "$regressed" ]; then
        echo "not updating: fix regressions first" >&2
        exit 1
    fi
    { head -1 "$KNOWN"; grep -v '^#' "$ACTUAL"; } > "$KNOWN.tmp" && mv "$KNOWN.tmp" "$KNOWN"
    echo "updated $KNOWN"
fi
