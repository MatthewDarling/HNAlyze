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

(defn object-ids
  [comments]
  (map :objectID comments))

(defn parent-ids
  [comments]
  (map :parent_id comments))

(defn all-object-ids
  [parsed-json]
  (map :objectID (all-comments parsed-json)))

(defn all-parent-ids
  [parsed-json]
  (map :parent_id (all-comments parsed-json)))

(defn unique-parents
  [parsed-json]
  (set (all-parent-ids parsed-json)))

(defn top-level-count
  [parsed-json]
  (first (sort-by val > (-> parsed-json
                            all-parent-ids
                            frequencies))))

(defn top-level-comments
  [parsed-json]
  (let [story (:story_id (first (all-comments parsed-json)))]
    (filter #(= story (:parent_id %)) (all-comments parsed-json))))

(defn comments-by-author
  [parsed-json author]
  (filter #(= author (:author %)) (all-comments parsed-json)))

(defn children-of-comment
  [parsed-json parent-id]
  (filter #(= parent-id (:parent_id %)) (all-comments parsed-json)))

(defn child-ids
  [parsed-json parent-id]
  (map :objectID (children-of-comment parsed-json parent-id)))
(defn direct-responses-to-author
  [parsed-json author]
  (-> parsed-json
      (comments-by-author author)
      object-ids
      ;;; You can thank/blame Achint Sandhu for inflicting this tricky macro usage on you
      (->> (map #(children-of-comment parsed-json %)))))
(defn comments-by-id
  "Returns a map of :objectID to comment."
  [parsed-json]
  (apply assoc {}
         (interleave (all-object-ids parsed-json)
                     (map #(comment-with-id parsed-json %)
                          (all-object-ids parsed-json)))))

(defn parent-and-children
  "Returns a map of parent_id to objectID of direct children."
  [parsed-json parent-id]
  (hash-map parent-id (child-ids parsed-json parent-id)))

(defn comments-tree
  "Returns a map of parent_id to objectID of all children."
  [parsed-json]
  (merge {(:story_id (first (all-comments parsed)))
          (map :objectID (top-level-comments parsed-json))}
         (apply merge
                (map #(parent-and-children parsed-json
                                           (:objectID %))
                     (all-comments parsed-json)))))
