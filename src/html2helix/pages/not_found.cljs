(ns html2helix.pages.not-found
  (:require [helix.dom :as d]
            [html2helix.macros :refer [defnc]]))

(defnc view []
  (d/h3 "404"))
