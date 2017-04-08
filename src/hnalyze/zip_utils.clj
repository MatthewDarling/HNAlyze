(ns hnalyze.zip-utils
  (:require [clojure.zip :as zip]))

(defn zipper-root?
  [loc]
  (= (zip/root loc) (zip/node loc)))

(defn zipper-first
  [loc]
  (if (zipper-root? loc)
    (zip/next loc)
    loc))

(defn node-val-except-end
  "Return the value at `zipper-node', or nil if it's the end of the
  zipper."
  [zipper-node]
  (when-not (zip/end? zipper-node)
    (zip/node zipper-node)))

(defn zipper-reduce
  "Like clojure.core/reduce, but reduces a zipper using a depth-first
  search. Behaviour should be identical to:

  (reduce f val (iterate clojure.zip/next loc))

  but without generating the intermediate sequence."
  ([f loc]
   (let [first-node (zipper-first loc)
         second-node (zip/next first-node)]
     (zipper-reduce f
                    (apply f (keep node-val-except-end [first-node second-node]))
                    (zip/next second-node))))
  ([f val loc]
   (loop [acc val
          current-loc (zipper-first loc)]
     (if (or (zip/end? current-loc) (reduced? acc))
       acc
       (recur (f acc (zip/node current-loc))
              (zip/next current-loc))))))
