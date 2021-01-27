(ns form-validation-example.subs
  (:require
   [re-frame.core :as re-frame]
   [form-validation-example.domain :as domain]

   [clojure.spec.alpha :as s]))

(re-frame/reg-sub
 ::name
 (fn [db]
   (:name db)))

(re-frame/reg-sub
 ::submit-state
 (fn [db]
   (:submit-state db)))

;; form
(re-frame/reg-sub
 ::form
 (fn [db]
   (:form db)))

(re-frame/reg-sub
 ::email
 :<- [::form]
 (fn [form]
   (:email form)))

(re-frame/reg-sub
 ::password
 :<- [::form]
 (fn [form]
   (:password form)))

(re-frame/reg-sub
 ::password-repeat
 :<- [::form]
 (fn [form]
   (:password-repeat form)))

(re-frame/reg-sub
 ::hobbies
 :<- [::form]
 (fn [form]
   (:hobbies form)))

(re-frame/reg-sub
 ::hobbies-count
 :<- [::hobbies]
 (fn [hobbies]
   (count hobbies)))

(re-frame/reg-sub
 ::hobby
 :<- [::hobbies]
 (fn [hobbies [_ idx]]
   (if (> (count hobbies) idx)
     (nth hobbies idx)
     {})))

(re-frame/reg-sub
 ::hobby-category
 :<- [::hobbies]
 (fn [hobbies [_ idx]]
   (when (> (count hobbies) idx)
     (:hobby-category (nth hobbies idx)))))

(re-frame/reg-sub
 ::hobby-links-count
 :<- [::hobbies]
 (fn [hobbies [_ idx]]
   (when (> (count hobbies) idx)
     (count (:hobby-links (nth hobbies idx))))))

(re-frame/reg-sub
 ::hobby-link
 :<- [::hobbies]
 (fn [hobbies [_ idx link-idx]]
   (if (and
        (>  (count hobbies) idx)
        (-> (nth hobbies idx)
            :hobby-links
            count
            (> link-idx)))
     (-> (nth hobbies idx)
         :hobby-links
         (nth link-idx)))))

(re-frame/reg-sub
 ::form-valid?
 :<- [::form]
 (fn [form]
   (s/valid? ::domain/form form)))

;; (assoc-in {:form {:data {:hobbies [{:label 0}]}}}
;;           (vec (concat [:form] (cons :data [:hobbies 1 :label]))) 1)

;; (print (re-frame/subscribe [::form-valid?]))
;; (print (s/explain-data ::domain/form @(re-frame/subscribe [::form])))

;; (re-frame/reg-sub
;;  ::email-is-valid?
;;  :<- [::form]
;;  (fn [form]
;;    (let [email (-> form :email)]
;;      (email-is-valid? email))))

;; (email-is-valid? 1)
;; (s/explain-data ::domain/password 1)
;;;; password


;; (re-frame/reg-sub
;;  ::password-is-valid?
;;  :<- [::form]
;;  (fn [form]
;;    (let [password (-> form :password)]
;;      (password-is-valid? password))))

;; (password-is-valid? 1)
;; (password-is-valid? "some password")
;; (password-is-valid? "some")
;;;; hobby

;; (re-frame/reg-sub
;;  ::hobbies
;;  :<- [::form]
;;  (fn [form]
;;    (:hobbies form)))


;; (re-frame/reg-sub
;;  ::hobbies-are-unique?
;;  :<- [::hobbies]
;;  (fn [hobbies]
;;    (hobbies-are-unique? hobbies)))

;;;; hobby-link

;; (hobbies-are-unique?
;;  [{:hobby-category-type :program
;;    :hobby-links
;;    ["https://github.com/megurumokke"]}
;;   {:hobby-category-type :program
;;    :hobby-links
;;    ["https://github.com/MeguruMokke"]}])

;; (hobbies-are-unique?
;;  [{:hobby-category-type :program
;;    :hobby-links
;;    ["https://github.com/megurumokke"]}
;;   {:hobby-category-type :illust
;;    :hobby-links
;;    ["https://github.com/MeguruMokke"]}])

;; form
