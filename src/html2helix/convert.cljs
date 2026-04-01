(ns html2helix.convert
  (:require [clojure.string :as str]
            [hickory.core :as h]))

(defn hickory->helix
  [element]
  (cond (string? element) (str/trim element)
        (map? element) (case (:type element)
                         :comment "TODO"
                         :document (some->> (:content element) (mapv hickory->helix))
                         :document-type "TODO"
                         :element (concat (list (symbol (str "d/" (name (:tag element)))))
                                          ;; TODO - rename keys for react
                                          ;; TODO - style map
                                          (some-> (:attrs element) (list))
                                          (some->> (:content element)
                                                   (map hickory->helix)
                                                   (remove empty?))))))

(defn html->helix
  [html]
  (let [helixes (->> (h/parse-fragment html)
                     (map (comp hickory->helix h/as-hickory))
                     (remove empty?))]
    (cond-> helixes (= (count helixes) 1) (first))))
