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
