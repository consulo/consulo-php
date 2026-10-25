#!/usr/bin/env bash
# Refreshes src/main/resources/phpstorm-stubs from a php-runtime-stubs (JetBrains phpstorm-stubs) checkout:
# the stub .php files and the JetBrains\PhpStorm attribute classes they use.
# Usage: ./sync-runtime-stubs.sh [path-to-php-runtime-stubs]   (default: ../../_PHP_WORLD/php-runtime-stubs, then ../../php-runtime-stubs)
set -euo pipefail

cd "$(dirname "$0")"
DEFAULT_SRC=../../_PHP_WORLD/php-runtime-stubs
[ -d "$DEFAULT_SRC" ] || DEFAULT_SRC=../../php-runtime-stubs
SRC="$(cd "${1:-$DEFAULT_SRC}" && pwd)"
DST=src/main/resources/phpstorm-stubs

rm -rf "$DST"
mkdir -p "$DST"
(cd "$SRC" && find . -name '*.php' -not -path './.*' -not -path './tests/*' -not -path './meta/*' -not -name 'PhpStormStubsMap.php' -print0 \
    && find ./meta/attributes -name '*.php' -print0) | (cd "$SRC" && cpio -0pdm --quiet "$OLDPWD/$DST")
cp "$SRC/LICENSE" "$DST/LICENSE"
git -C "$SRC" log -1 --format='%H %cd' --date=short > "$DST/REVISION"

echo "copied $(find "$DST" -name '*.php' | wc -l) stubs from $SRC at $(cat "$DST/REVISION")"
