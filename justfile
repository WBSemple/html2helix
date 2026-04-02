default:
    @just --list

deps:
    npm ci
    rm -rf public/fonts
    mkdir public/fonts
    cp node_modules/@fontsource-variable/league-spartan/files/league-spartan-latin-wght-normal.woff2 \
      public/fonts/

run:
    npx shadow-cljs -A:dev clj-run html2helix.shadow/watch

build: deps
    rm -rf public/output.css public/js
    npx shadow-cljs release app
    npx @tailwindcss/cli -i resources/input.css -o public/output.css
    sed "s/LASTMOD/$(date +'%Y-%m-%d')/g" resources/sitemap.xml > public/sitemap.xml
