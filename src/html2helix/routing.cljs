(ns html2helix.routing
  (:require [refx.alpha :as r]
            [reitit.frontend.controllers :as rfc]
            [reitit.frontend.easy :as rfe]))

(r/reg-fx ::push-state*
  (fn [route]
    (apply rfe/push-state route)))

(r/reg-event-fx ::push-state
  (fn [_ [_ & route]]
    {::push-state* route}))

(r/reg-fx ::set-title
  (fn [title]
    (set! (.-title js/document) title)))

(r/reg-event-fx ::navigated
  (fn [{:keys [db] :as _cofx} [_ new-match]]
    (let [old-match (::current-route db)
          controllers (rfc/apply-controllers (:controllers old-match) new-match)]
      {:db (assoc db ::current-route (assoc new-match :controllers controllers))
       ::set-title (get-in new-match [:data :title])})))

(r/reg-sub ::current-route
  (fn [db]
    (::current-route db)))

(defn on-navigate
  [new-match _history]
  (if new-match
    (r/dispatch [::navigated new-match])
    (r/dispatch [::push-state ::not-found])))

(defn start-routing!
  [router]
  (rfe/start! router on-navigate {:use-fragment false}))
