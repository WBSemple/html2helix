(ns html2helix.refresh
  (:require [dataspex.core :as dataspex]
            [helix.experimental.refresh :as r]
            [refx.db :as db]))

(r/inject-hook!)

(defn ^:dev/after-load refresh []
  (r/refresh!))

(dataspex/inspect "App DB" db/app-db)
