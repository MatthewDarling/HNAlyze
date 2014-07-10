(ns hnalyze.core
  (:require [cheshire.core :as ch]
            [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.pprint :refer :all])
  (:gen-class))
(defn get-comments
  [story-id]
  (slurp (str "https://hn.algolia.com/api/v1/search?tags=comment,story_"
              story-id
              "&hitsPerPage=500")))

(defn clean-api-json
  "Remove opening parentheses because they kill the JSON parser,
  but closing parens are okay because... who knows. Also remove the
  quotes around the objectID field so it can be a plain integer."
  [api-json]
  (string/replace
   (string/replace api-json #"\(" "")
   #"(objectID.:).(\d+).," "$1$2,"))

(defn parse-json
  [api-json]
  (ch/parse-string (clean-api-json api-json) true))

(defn pprint-to-file
  [x filename]
  (->> x
       pprint
       with-out-str
       (spit filename)))

(defn all-comments
  [parsed-json]
  (second (first parsed-json)))

(defn all-authors
  [parsed-json]
  (->> parsed-json
       all-comments
       (map :author)
       set))

(defn comment-with-id
  [parsed-json object-id]
  (filter #(= object-id (:objectID %)) (all-comments parsed-json)))

(defn author-contributions
  [parsed-json]
  (->> parsed-json
       all-comments
       (map :author)
       frequencies))

(defn sorted-author-counts
  [parsed-json]
  (sort-by val > (author-contributions parsed-json)))
