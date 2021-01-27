(ns form-validation-example.views
  (:require
   [re-frame.core :as re-frame]
   [goog.string :as gstring]
   [form-validation-example.domain :as domain]
   [form-validation-example.subs :as subs]
   [form-validation-example.events :as events]
   [form-validation-example.valids :as valids]
   [clojure.spec.alpha :as s]))

(defn email-field []
  (let [email (re-frame/subscribe [::subs/email])]
    (fn []
      [:div.field
       [:label.label "email"]
       [:div.control
        [:input
         {:type "text" :placeholder "some@address.com"
          :class (if (valids/email-is-valid? @email) "input is-danger" "input")
          :value @email
          :on-change #(re-frame/dispatch [::events/email (-> % .-target .-value)])}]
        (when-let [error-message (valids/email-is-valid? @email)]
          [:p.help.is-danger error-message])]])))

(defn password-field []
  (let [password (re-frame/subscribe [::subs/password])
        password-repeat (re-frame/subscribe [::subs/password-repeat])]
    (fn []
      [:div.field
       [:label.label "password"]
       [:div.control
        [:input.input {:type "password" :placeholder "password"
                       :class (if (valids/password-is-valid? @password) "input is-danger" "input")
                       :value @password
                       :on-change #(re-frame/dispatch [::events/password (-> % .-target .-value)])}]
        (when-let [error-message (valids/password-is-valid? @password)]
          [:p.help.is-danger error-message])]
       [:label.label "password (repeat)"]
       [:div.control
        [:input.input {:type "password" :placeholder "password (repeat)"
                       :value @password-repeat
                       :on-change #(re-frame/dispatch [::events/password-repeat (-> % .-target .-value)])}]

        (when-let [error-message (valids/password-is-repeated? @password @password-repeat)]
          [:p.help.is-danger error-message])]])))

(defn hobby-category [category-key category-value]
  [:option {:key category-key :value category-key} category-value])

(defn hobby-selector [idx]
  (let [category (re-frame/subscribe [::subs/hobby-category idx])
        hobbies (re-frame/subscribe [::subs/hobbies])]
    (fn []
      [:div.control
       [:span.select
        [:select {:on-change #(re-frame/dispatch [::events/hobby-category idx (keyword (-> % .-target .-value))])
                  :value (if @category (name @category) "---")}
         [:option {:value "---"} "---"]
         [:<>
          (map (fn [[category-key category-value]]
                 ^{:key category-key}
                 [hobby-category category-key category-value])
               domain/hobby-categories)]]]
       (when-let [error-message (valids/hobbies-are-unique? @hobbies)]
         [:p.help.is-danger error-message])])))

(defn hobby-link [idx link-idx]
  (let [hobby-link (re-frame/subscribe [::subs/hobby-link idx link-idx])
        hobby-links-count (re-frame/subscribe [::subs/hobby-links-count idx])]
    (fn []
      [:div.field.is-grouped
       [:p.control.is-expanded
        [:input {:type "text" :placeholder "https://somelink.com"
                 :class (if (valids/hobby-link-is-valid? @hobby-link) "input is-danger" "input")
                 :value @hobby-link
                 :on-change #(re-frame/dispatch [::events/hobby-link idx link-idx (-> % .-target .-value)])}]]
       (when-let [error-message (valids/hobby-link-is-valid? @hobby-link)]
         [:p.help.is-danger error-message])
       [:p.control
        [:a.button.is-info
         {:on-click #(re-frame/dispatch [::events/remove-hobby-link idx link-idx])}
         [:i.fa.fa-trash]]]])))

(defn hobby-links [idx]
  (let [hobby-links-count (re-frame/subscribe [::subs/hobby-links-count idx])]
    (fn []
      [:div.control
       [:label.label "hobby's links"]
       (->> (range (max 1 @hobby-links-count))
            (map (fn [i]
                   ^{:key (gstring/format "%d-%d" idx i)}
                   [hobby-link idx i])))
       (if (< @hobby-links-count 3)
         [:a.button.is-info
          {:on-click #(re-frame/dispatch [::events/hobby-link idx  @hobby-links-count ""])}
          "+"])])))

(defn hobby-field [idx]
  [:div.field
   [:hr]
   [:label.label "Hobby: " idx
    [:a.button.is-info {:style {:float "right"}} [:i.fa.fa-trash]]]
   [hobby-selector idx]
   [hobby-links idx]
   [:hr]])

(defn hobbies-field []
  (let [hobbies-count (re-frame/subscribe [::subs/hobbies-count])]
    [:<>
     [:div.field
      [:label.label "Hobbies"]
      (->> (range (max 1 @hobbies-count))
           (map (fn [idx]
                  ^{:key (gstring/format "%d" idx)}
                  [hobby-field idx])))]
     [:button.button.is-primary
      {:on-click #(re-frame/dispatch [::events/hobby @hobbies-count {:hobby-links []}])} "+"]]))

(defn submit-field []
  (let [form-valid? (re-frame/subscribe [::subs/form-valid?])
        submit-state (re-frame/subscribe [::subs/submit-state])]
    (fn []
      [:div.field
       [:button.button
        {:disabled  (not @form-valid?)
         :on-click #(do
                      (re-frame/dispatch [::events/submit-state "loading"])
                      (re-frame/dispatch [::events/submit]))}
        "submit"]
       [:p "submit status: " @submit-state]])))

(defn raw-data-field []
  (let [form (re-frame/subscribe [::subs/form])]
    (fn []
      [:code.clojure {:style {:white-space "pre"}}
       (with-out-str (cljs.pprint/pprint @form))])))

(defn main-panel []
  (let [name (re-frame/subscribe [::subs/name])]
    [:div.container.mx-5.mt-5
     [:h1 "Form Example with " @name]
     [:hr]
     [email-field]
     [:hr]
     [password-field]
     [:hr]
     [hobbies-field]
     [:hr]
     [submit-field]
     [raw-data-field]]))
