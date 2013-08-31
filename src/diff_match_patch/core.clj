(ns diff-match-patch.core  
  (:import [name.fraser.neil.plaintext diff_match_patch]))


(defn make-diffor []
  "Initializes and returns new diff-match-patch object"
  (diff_match_patch.))

(def ^:dynamic *diffor* (make-diffor))

(defn get-diffor []
  *diffor*)

(load "diff")
(load "match")
(load "patch")

