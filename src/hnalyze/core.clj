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
