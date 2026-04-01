(ns html2helix.macros
  #?(:clj (:require [helix.core :as helix]))
  #?(:cljs (:require-macros [html2helix.macros])))

#?(:clj
   (defmacro defnc [type params & body]
     (let [opts? (map? (first body)) ;; whether an opts map was passed in
           opts (if opts? (first body) {})
           body (if opts? (rest body) body)
           ;; feature flags to enable by default
           default-opts {:helix/features {:fast-refresh true}}]
       `(helix/defnc ~type ~params
          ~(merge default-opts opts)
          ~@body))))
