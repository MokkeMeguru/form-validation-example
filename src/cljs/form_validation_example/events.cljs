(ns form-validation-example.events
  (:require
   [re-frame.core :as re-frame]
   [form-validation-example.db :as db]
   [form-validation-example.utils :as utils]
   [day8.re-frame.tracing :refer-macros [fn-traced]]))

(re-frame/reg-event-db
 ::initialize-db
 (fn-traced [_ _]
            db/default-db))

(re-frame/reg-event-db
 ::email
 (fn [db [_ email]]
   (assoc-in db [:form :email] email)))

(re-frame/reg-event-db
 ::password
 (fn [db [_ password]]
   (assoc-in db [:form :password] password)))

(re-frame/reg-event-db
 ::password-repeat
 (fn [db [_ password]]
   (assoc-in db [:form :password-repeat] password)))

(re-frame/reg-event-db
 ::hobby-link
 (fn [db [_ idx link-idx hobby-link]]
   (assoc-in db [:form :hobbies idx :hobby-links link-idx] hobby-link)))

(re-frame/reg-event-db
 ::remove-hobby-link
 (fn [db [_ idx link-idx]]
   (update-in db [:form :hobbies idx :hobby-links] utils/drop-index link-idx)))

(re-frame/reg-event-db
 ::hobby-category
 (fn [db [_ idx category]]
   (assoc-in db [:form :hobbies idx :hobby-category] category)))

(re-frame/reg-event-db
 ::hobby
 (fn [db [_  hobbies-count hobby]]
   (assoc-in db [:form :hobbies hobbies-count] hobby)))

(re-frame/reg-event-db
 ::submit-state
 (fn [db [_ state]]
   (assoc db :submit-state state)))

(re-frame/reg-event-fx
 ::submit
 (fn [_ _]
   {:dispatch-later [{:ms 500
                      :dispatch [::submit-state "completed"]}]}))
