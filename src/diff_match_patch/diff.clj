(ns diff-match-patch.diff
  (:import [name.fraser.neil.plaintext diff_match_patch diff_match_patch$Operation]))

(defn make-diffor []
  "Initializes and returns new diff-match-patch object"
  (diff_match_patch.))

(def ^:dynamic *diffor* (make-diffor))

(defn main [txt1 txt2] 
   "Finds the differences between 2 texts."
    (.diff_main *diffor* txt1 txt2))

(defmulti cleanup!  
  "Reduces the number of edits by eliminating semantically trivial equalities"
  (fn [cleanup-type diffs] (identity cleanup-type)))

(defmethod cleanup! :semantic [cleanup-type diffs]
  (do
    (.diff_cleanupSemantic *diffor* diffs)
    diffs))

(defmethod cleanup! :lossless [cleanup-type diffs]
  (do
    (.diff_cleanupSemanticLossless diffs)
    diffs))

(defmethod cleanup! :efficiency [cleanup-type diffs]
  (do
    (.diff_cleanupEfficiency *diffor* diffs)
    diffs))

(defmethod cleanup! :merge [cleanup-type diffs]
  (do
    (.diff_cleanupMerge *diffor* diffs)
    diffs))

(defmethod cleanup! :default [cleansing-type diffs]
  :oops)

