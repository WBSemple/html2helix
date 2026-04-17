(ns html2helix.convert
  (:require [cljs.pprint :as pprint]
            [clojure.string :as str]
            [hickory.core :as h]
            [medley.core :refer [update-existing]]))

(defn- parse-inline-style
  [s]
  (into {}
        (comp (map str/trim)
              (remove empty?)
              (keep #(let [[k v] (str/split % #":" 2)]
                       (when v [(keyword k) (str/trim v)]))))
        (str/split s #";")))

(defn- hickory->helix
  [alias node]
  (cond (string? node) (str/trim node)
        (map? node) (case (:type node)
                      :comment (list 'comment (str/join " " (map str/trim (:content node))))
                      :element (concat (list (symbol (str alias \/ (name (:tag node)))))
                                       (some-> (:attrs node) (update-existing :style parse-inline-style) (list))
                                       (some->> (:content node)
                                                (map #(hickory->helix alias %))
                                                (remove empty?))))))

(defn html->helix
  [alias html]
  (transduce (comp (map #(hickory->helix alias (h/as-hickory %)))
                   (remove empty?)
                   (map #(pprint/write % :stream nil))
                   (interpose "\n\n"))
             str
             (h/parse-fragment html)))
