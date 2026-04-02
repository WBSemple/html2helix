(ns html2helix.refresh
  (:require [helix.experimental.refresh :as r]))

(r/inject-hook!)

(defn ^:dev/after-load refresh []
  (r/refresh!))
