(ns html2helix.core
  (:require [helix.core :refer [$]]
            ["react-dom/client" :as rdom]
            [html2helix.app :as app]
            [html2helix.pages.home :as home]
            [html2helix.pages.not-found :as not-found]
            [html2helix.routing :as routing]
            [refx.alpha :as r]
            [reitit.frontend :as rf]))

(def routes
  ["/"
   [""
    {:name ::routing/home
     :title "html2helix"
     :view home/view}]

   ["not-found"
    {:name ::routing/not-found
     :title "html2helix - 404"
     :view not-found/view}]])

(defn ^:export init []
  (r/clear-subscription-cache!)
  (let [router (rf/router routes)]
    (routing/start-routing! router)
    (doto (rdom/createRoot (js/document.getElementById "app"))
      (.render ($ app/app)))))
