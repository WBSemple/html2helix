(ns html2helix.core
  (:require [helix.core :refer [$]]
            ["react-dom/client" :as rdom]
            [html2helix.app :as app]
            [refx.alpha :as r]))

(defn ^:export init []
  (r/clear-subscription-cache!)
  (doto (rdom/createRoot (js/document.getElementById "app"))
    (.render ($ app/app))))
