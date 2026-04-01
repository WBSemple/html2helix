(ns html2helix.app
  (:require [helix.core :refer [$ <>]]
            [helix.dom :as d]
            [refx.alpha :as r]
            [html2helix.macros :refer [defnc]]
            [html2helix.routing :as routing]))

(defnc app []
  (let [current-route (r/use-sub [::routing/current-route])]
    (d/div {:className "mx-auto px-4 pb-6"}
     (when current-route
       (-> current-route :data :view $)))))
