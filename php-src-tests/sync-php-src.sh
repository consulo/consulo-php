#!/usr/bin/env bash
# Refreshes src/test/resources/php-src from a php-src checkout (only the .phpt tests the parser corpus uses).
# Usage: ./sync-php-src.sh [path-to-php-src]   (default: ../../../php-src)
set -euo pipefail

cd "$(dirname "$0")"
SRC="$(cd "${1:-../../../php-src}" && pwd)"
DST=src/test/resources/php-src

rm -rf "$DST"/Zend "$DST"/tests
mkdir -p "$DST"
(cd "$SRC" && find Zend/tests tests -name '*.phpt' -print0) | (cd "$SRC" && cpio -0pdm --quiet "$OLDPWD/$DST")
cp "$SRC/LICENSE" "$DST/LICENSE"
git -C "$SRC" log -1 --format='%H %cd' --date=short > "$DST/REVISION"

echo "copied $(find "$DST" -name '*.phpt' | wc -l) tests from $SRC at $(cat "$DST/REVISION")"
