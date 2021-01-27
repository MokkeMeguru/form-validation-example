(ns form-validation-example.core-test
  (:require [cljs.test :refer-macros [deftest testing is]]
            [form-validation-example.core :as core]))

(deftest fake-test
  (testing "fake description"
    (is (= 1 2))))
