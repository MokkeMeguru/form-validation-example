(ns form-validation-example.domain
  (:require [clojure.spec.alpha :as s]))

;; email
(s/def ::email
  (s/and string? (partial re-matches #"^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,63}$")))

;; password
(s/def ::password-length #(<= 6 (count %) 255))
(s/def ::password (s/and string? ::password-length))
(s/def ::password-repeat (s/and string? ::password-length))

;; hobby

;;;; hobby category
(def hobby-categories
  {:program "プログラミング" :illust "イラスト" :novel "小説"})

(s/def ::hobby-category (-> hobby-categories keys set))

;;;; hobby category link
(s/def ::hobby-link
  (s/and string? (partial re-matches #"^https?://[\w!\?/\+\-_~=;\.,\*&@#\$%\(\)'\[\]\u3000-\u30FE\u4E00-\u9FAF\uF900-\uFA2F\uFF01-\uFFEE]+")))

(s/def ::hobby-links (s/coll-of ::hobby-link :distinct true))

(defn unique-hobbies [hobbies]
  (apply distinct? (map #(:hobby-category %) hobbies)))

(s/def ::unique-hobbies unique-hobbies)

(s/def ::hobby (s/keys :req-un [::hobby-category ::hobby-links]))

(s/def ::hobbies (s/and (s/coll-of ::hobby :distinct true) ::unique-hobbies))

(s/def ::form (s/keys :req-un [::email ::password ::password ::hobbies]))

;; playground
;; (def create-payload-example
;;   {:email "meguru.mokke@gmail.com"
;;    :password "password"
;;    :password-repeat "password"
;;    :hobbies
;;    [{:hobby-category :program
;;      :hobby-links
;;      ["https://github.com/MeguruMokke"]}
;;     {:hobby-category :illust
;;      :hobby-links
;;      ["https://pixiv.com/users/xxxxxx"]}]})

;; (s/explain-data ::hobbies
;;                 [{:hobby-category :program
;;                   :hobby-links
;;                   ["https://github.com/megurumokke"]}
;;                  {:hobby-category :program
;;                   :hobby-links
;;                   ["https://github.com/MeguruMokke"]}])

;; (s/explain-data ::form create-payload-example)

;; (s/def ::sample-list (s/coll-of ::email :distinct true))
;; (s/explain-data ::sample-list ["test@gmail.com" "test@gmail.com"])
