(ns hnalyze.zip-utils-test
  (:require [hnalyze.zip-utils :as zip-utils]
            [clojure.test :as t]
            [clojure.zip :as zip]))

;;; Cribbed from the tests for reduce:
;; https://github.com/clojure/clojure/blob/clojure-1.9.0-alpha14/test/clojure/test_clojure/sequences.clj#L28
(t/deftest test-zipper-reduce
  (let [arange (range 1 100)
        arange-zip (zip/vector-zip (vec arange))
        all-true (vec (repeat 10 true))
        all-true-zip (zip/vector-zip all-true)]
    (t/is (== 4950
              (reduce + arange)
              (zip-utils/zipper-reduce + arange-zip)))
    (t/is (== 4951
              (reduce + 1 arange)
              (zip-utils/zipper-reduce + 1 arange-zip)))
    (t/is (= true
             (reduce #(and %1 %2) all-true)
             (reduce #(and %1 %2) true all-true)
             (zip-utils/zipper-reduce #(and %1 %2) all-true-zip)
             (zip-utils/zipper-reduce #(and %1 %2) true all-true-zip)))))
