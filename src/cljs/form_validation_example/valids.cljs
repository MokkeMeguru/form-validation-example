(ns form-validation-example.valids
  (:require
   [form-validation-example.domain :as domain]
   [clojure.spec.alpha :as s]))

(defn email-is-valid? [email]
  (when-not (empty? email)
    (when-let [problems (s/explain-data ::domain/email email)]
      (let [via (-> problems :cljs.spec.alpha/problems first :via reverse first)]
        (cond
          (= via ::domain/email) "不正なメールアドレスです。"
          :else "不明なエラーです。別のアドレスを入力してください。")))))

(defn password-is-valid? [password]
  (when-not (empty? password)
    (when-let [problems (s/explain-data ::domain/password password)]
      (let [via (-> problems :cljs.spec.alpha/problems first :via reverse first)]
        (cond
          (= via ::domain/password-length) "パスワードは6文字以上256文字未満の文字列です。"
          (= via ::domain/password) "不正なパスワードです。"
          :else "不明なエラーです。別のパスワードを入力してください。")))))

(defn hobbies-are-unique? [hobbies]
  (when-not (empty? hobbies)
    (when-let [problems (s/explain-data ::domain/unique-hobbies hobbies)]
      (let [via (-> problems :cljs.spec.alpha/problems first :via reverse first)]
        (cond
          (= via ::domain/unique-hobbies) "hobby の重複はできません。"
          :else "不明なエラーです。hobby の入力を見直してください。")))))

(defn hobby-link-is-valid? [hobby-link]
  (when-not (empty? hobby-link)
    (when-let [problems (s/explain-data ::domain/hobby-link hobby-link)]
      (let [via (-> problems :cljs.spec.alpha/problems first :via reverse first)]
        (cond
          (= via ::domain/hobby-link) "不正なリンクです。 http [s] アドレスを入力してください。"
          :else "不明なエラーです。リンクを見直してください。")))))

(defn form-is-valid? [form]
  (when-let [problems (s/explain-data ::domain/form form)]
    "入力に問題があります。"))

(defn password-is-repeated? [password password-repeat]
  (when-not (empty? password-repeat)
    (when (not= password password-repeat)
      "パスワードが一致しません。")))
