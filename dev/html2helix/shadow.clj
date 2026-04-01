(ns html2helix.shadow
  (:require [shadow.cljs.devtools.api :as shadow]))

(defn watch
  {:shadow/requires-server true}
  []
  (let [cmd ["npx" "@tailwindcss/cli" "-i" "resources/input.css" "-o" "public/output.css" "--watch"]
        builder (-> (ProcessBuilder/new ^String/1 (into-array String cmd))
                    (ProcessBuilder/.inheritIO))
        process (ProcessBuilder/.start builder)]
    (Runtime/.addShutdownHook (Runtime/getRuntime) (Thread/new (fn [] (Process/.destroy process))))
    (shadow/watch :app)))
