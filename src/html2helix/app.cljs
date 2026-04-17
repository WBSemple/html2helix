(ns html2helix.app
  (:require ["@uiw/react-codemirror$default" :as CodeMirror]
            ["@codemirror/language" :refer [StreamLanguage]]
            ["@codemirror/state" :refer [EditorState]]
            ["@codemirror/lang-html" :as lang-html]
            ["@nextjournal/lang-clojure" :as lang-clj]
            [helix.core :refer [$ <>]]
            [helix.dom :as d]
            [helix.hooks :as h]
            [html2helix.convert :as convert]
            [html2helix.macros :refer [defnc]]))

(def example "<div class=\"my-class\">\n  <!-- I'm a comment -->\n  <h1 style=\"color: coral; font-size: 24px;\">\n    Hello, world!\n  </h1>\n</div>")

(defnc clipboard-icon []
  (d/svg
    {:xmlns "http://www.w3.org/2000/svg" :fill "none" :viewBox "0 0 24 24" :stroke-width "1.5" :stroke "currentColor"
     :class "size-6"}
    (d/path {:stroke-linecap "round"
             :stroke-linejoin "round"
             :d "M8.25 7.5V6.108c0-1.135.845-2.098 1.976-2.192.373-.03.748-.057 1.123-.08M15.75 18H18a2.25 2.25 0 0 0 2.25-2.25V6.108c0-1.135-.845-2.098-1.976-2.192a48.424 48.424 0 0 0-1.123-.08M15.75 18.75v-1.875a3.375 3.375 0 0 0-3.375-3.375h-1.5a1.125 1.125 0 0 1-1.125-1.125v-1.5A3.375 3.375 0 0 0 6.375 7.5H5.25m11.9-3.664A2.251 2.251 0 0 0 15 2.25h-1.5a2.251 2.251 0 0 0-2.15 1.586m5.8 0c.065.21.1.433.1.664v.75h-6V4.5c0-.231.035-.454.1-.664M6.75 7.5H4.875c-.621 0-1.125.504-1.125 1.125v12c0 .621.504 1.125 1.125 1.125h9.75c.621 0 1.125-.504 1.125-1.125V16.5a9 9 0 0 0-9-9Z"})))

(defnc github-icon []
  (d/svg {:xmlns "http://www.w3.org/2000/svg" :width "16" :height "16" :fill "currentColor" :viewBox "0 0 16 16"}
    (d/path {:d "M8 0C3.58 0 0 3.58 0 8c0 3.54 2.29 6.53 5.47 7.59.4.07.55-.17.55-.38 0-.19-.01-.82-.01-1.49-2.01.37-2.53-.49-2.69-.94-.09-.23-.48-.94-.82-1.13-.28-.15-.68-.52-.01-.53.63-.01 1.08.58 1.23.82.72 1.21 1.87.87 2.33.66.07-.52.28-.87.51-1.07-1.78-.2-3.64-.89-3.64-3.95 0-.807.31-1.59.82-2.15-.08-.2-.36-1.02.08-2.12 0 0 .67-.21 2.2.82.64-.18 1.32-.27 2-.27s1.36.09 2 .27c1.53-1.04 2.2-.82 2.2-.82.44 1.1.16 1.92.08 2.12.51.56.82 1.27.82 2.15 0 3.07-1.87 3.75-3.65 3.95.29.25.54.73.54 1.48 0 1.07-.01 1.93-.01 2.2 0 .21.15.46.55.38A8.01 8.01 0 0 0 16 8c0-4.42-3.58-8-8-8"})))

(defnc code-blocks []
  (let [[html setHtml] (h/use-state example)
        [alias setAlias] (h/use-state "d")
        helix (some->> (not-empty html)
                       (convert/html->helix alias))]
    (d/div {:class "grid lg:grid-cols-2 gap-x-6 mt-3"}
      (d/label {:class "lg:order-1 my-auto"} "HTML")
      (d/div {:class "lg:order-3 overflow-auto pb-2"}
        ($ CodeMirror {:className "border border-base-300 rounded-field overflow-auto"
                       :value html
                       :onChange #(setHtml %)
                       :extensions #js [(lang-html/html)]}))
      (d/div {:class "lg:order-2"}
        (d/label {:class "input input-sm w-auto inline-block tooltip tooltip-top"
                  :data-tip "customise helix.dom alias"}
          (d/code
            "[helix.dom :as "
            (d/input {:type "text"
                      :placeholder "d"
                      :on-change #(setAlias (or (not-empty (.. % -target -value)) "d"))
                      :style {:width (str (count alias) "ch")}})
            "]"))
        (let [[copied setCopied] (h/use-state false)]
          (d/div {:class "tooltip tooltip-left float-right"
                  :data-tip (if copied "copied" "copy")}
            (d/button {:class "btn btn-link btn-sm text-base-content ps-0 pe-2 float-right"
                       :on-click #(-> (.writeText js/navigator.clipboard helix)
                                      (.then (setCopied true)
                                             (js/setTimeout (fn [] (setCopied false)) 2000)))}
              ($ clipboard-icon)))))
      (d/div {:class "lg:order-4 overflow-auto pb-2"}
        ($ CodeMirror {:className "border border-base-300 rounded-field overflow-auto"
                       :value (str helix)
                       :extensions #js [(lang-clj/clojure) (.of EditorState.readOnly true)]})))))

(defnc app []
  (d/div {:class "flex flex-col h-screen justify-between"}
    (d/div {:class "px-4 pb-6"}
      (d/h1 {:class "text-4xl py-2 text-center font-medium"}
        "html2helix")
      (d/div {:class "flex justify-center"}
        (d/h2 {:class "badge badge-soft badge-primary inline-block"}
          "Convert raw HTML to ClojureScript "
          (d/a {:class "link"
                :href "https://github.com/lilactown/helix"
                :target "_blank"}
            "Helix")
          " syntax"))
      ($ code-blocks))
    (d/footer
      {:class "footer footer-center bg-base-300 p-2"}
      (d/a {:class "link flex"
            :href "https://github.com/WBSemple/html2helix"
            :target "_blank"}
        ($ github-icon)
        "source"))))
