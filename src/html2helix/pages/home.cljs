(ns html2helix.pages.home
  (:require ["@heroicons/react/24/outline" :as hero]
            ["@uiw/react-codemirror$default" :as CodeMirror]
            ["@codemirror/language" :refer [StreamLanguage]]
            ["@codemirror/state" :refer [EditorState]]
            ["@codemirror/lang-html" :as lang-html]
            ["@nextjournal/lang-clojure" :as lang-clj]
            [helix.core :refer [$ <>]]
            [helix.dom :as d]
            [html2helix.convert :as convert]
            [html2helix.macros :refer [defnc]]
            [refx.alpha :as r]))

(r/reg-sub ::html
  (fn [db _]
    (get db ::html "")))

(r/reg-sub ::hickory
  (fn [_]
    (r/sub [::html]))
  (fn [html _]
    (some-> (not-empty html)
            (convert/html->helix))))

(r/reg-event-db ::set-html
  (fn [db [_ html]]
    (assoc db ::html html)))

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
    ;; TODO - specify helix.dom alias?
    (d/div {:className "grid lg:grid-cols-2 gap-6 mt-3"}
      (d/div
        (d/label "HTML")
        ($ CodeMirror {:className "border border-base-300 rounded overflow-auto"
                       :onChange #(r/dispatch [::set-html %])
                       :extensions #js [(lang-html/html)]}))
      (d/div
        (d/label "Helix") ;; TODO - clipboard button
        (let [hick (r/use-sub [::hickory])]
          ($ CodeMirror {:className "border border-base-300 rounded overflow-auto"
                         :value (str hick)
                         :extensions #js [(lang-clj/clojure) (.of EditorState.readOnly true)]}))))))
