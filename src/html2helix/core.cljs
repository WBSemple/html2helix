(ns html2helix.core
  (:require [helix.core :refer [$]]
            ["react-dom/client" :as rdom]
            [html2helix.app :as app]))

(defn ^:export init []
  (doto (rdom/createRoot (js/document.getElementById "app"))
    (.render ($ app/app))))
