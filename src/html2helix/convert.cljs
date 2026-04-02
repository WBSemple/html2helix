(ns html2helix.convert
  (:require [camel-snake-kebab.core :as csk]
            [cljs.pprint :as pprint]
            [clojure.set :as set]
            [clojure.string :as str]
            [hickory.core :as h]
            [medley.core :refer [update-existing]]))

(defn- parse-inline-style
  [s]
  (into {}
        (comp (map str/trim)
              (remove empty?)
              (keep #(let [[k v] (str/split % #":" 2)]
                       (when v [(csk/->camelCaseKeyword k) (str/trim v)]))))
        (str/split s #";")))

(defn- format-attrs
  [attrs]
  (-> (set/rename-keys attrs {:class :className
                              :for :htmlFor})
      (update-existing :style parse-inline-style)))

(defn- hickory->helix
  [node]
  (cond (string? node) (str/trim node)
        (map? node) (case (:type node)
                      :comment (list 'comment (str/join " " (map str/trim (:content node))))
                      :element (concat (list (symbol (str "d/" (name (:tag node)))))
                                       (some-> (:attrs node) (format-attrs) (list))
                                       (some->> (:content node)
                                                (map hickory->helix)
                                                (remove empty?))))))

(defn html->helix
  [html]
  (transduce (comp (map (comp hickory->helix h/as-hickory))
                   (remove empty?)
                   (map #(pprint/write % :stream nil))
                   (interpose "\n\n"))
             str
             (h/parse-fragment html)))
