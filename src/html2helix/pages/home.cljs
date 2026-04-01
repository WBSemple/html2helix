(ns html2helix.pages.home
  (:require ["@heroicons/react/24/outline" :as hero]
            ["@uiw/react-codemirror$default" :as CodeMirror]
            ["@codemirror/language" :refer [StreamLanguage]]
            ["@codemirror/lang-html" :as lang-html]
            ["@nextjournal/lang-clojure" :as lang-clj]
            [helix.core :refer [$ <>]]
            [helix.dom :as d]
            [html2helix.macros :refer [defnc]]))




(defnc view []
  (<>
    (d/h1 {:className "text-4xl py-2 text-center font-medium"}
      "html2helix")
    (d/div {:className "flex justify-center"}
      (d/h2 {:className "badge badge-soft badge-primary inline-block"}
        "Convert raw HTML to Clojurescript "
        (d/a {:className "link"
              :href "https://github.com/lilactown/helix"
              :target "_blank"}
          "Helix")
        " syntax"))
    (d/div {:className "grid lg:grid-cols-2 gap-6 mt-3"}
      (d/div
        (d/label "HTML")
        ($ CodeMirror {:className "border border-base-300 rounded overflow-auto"
                       :extensions #js [(lang-html/html)]}))
      (d/div
        (d/label "Helix")
        ($ CodeMirror {:className "border border-base-300 rounded overflow-auto"
                       :extensions #js [(lang-clj/clojure)]})))))
