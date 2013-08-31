(ns diff-match-patch.core
  (:import [name.fraser.neil.plaintext diff_match_patch$Operation])
  (:require [clojure.string :as string]))

(defn diff [txt1 txt2] 
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
    (.diff_cleanupSemanticLossless *diffor* diffs)
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

(defn levenshtein [diffs]
  (.diff_levenshtein *diffor* diffs))

(defn common-prefix [^:String txt1 ^:String txt2]
  "Determines the common prefix of 2 strings."
  (.diff_commonPrefix *diffor* txt1 txt2))

(defn common-suffix [^:String txt1 ^:String txt2]
  "Determines the common suffix of two strings."
  (.diff_commonSuffix *diffor* txt1 txt2))

(defn x-index [diffs, ^:Integer loc]
  "Computes and returns the equivalent location in text2 for location is text1"
  (.diff_xIndex *diffor* diffs loc))

(defn pretty-html [diffs]
  "Converts a diff list into a pretty HTML report."
  (.diff_prettyHtml *diffor* diffs))

(defn to-source-text [diffs]
  "Computes and returns the source text from list of diff objects."
  (.diff_text1 *diffor* diffs))

(defn to-destination-text [diffs]
  "Computes and returns the destination text"
  (.diff_text2 *diffor* diffs))

(defn to-delta [diffs]
  "Crushes the diff into and encoded string which describes the operations
  required to transform text1 into text2.
  Operations are tab-separated. Inserted text is escaped using %xx notation.
  "
  (.diff_toDelta *diffor* diffs))

(defn from-delta [^:String text1 ^:String delta-text]
  "given the original text1 and an encoded string which describes the operations
  required to transform text1 into text2, and computes the full diff."
  (.diff_fromDelta *diffor* text1 delta-text))

(defn- get-op [diff]
  (let [op-string (.. diff operation toString)]
    (-> op-string str string/lower-case keyword)))

(defn- get-val [diff]
  (.text diff))

(defn to-hiccup [diffs]
  "Converts a diff list into a pretty Hiccup presentation"
  (let [pos (atom 0)]
    (for [diff diffs]
      (let [op-key (get-op diff)
            diff-val (get-val diff)
            diff-length (count diff-val)
            current-pos @pos]
        (when-not (= :delete op-key)
          (swap! pos #(+ diff-length %1)))
        (vector op-key 
                {:class (name op-key)
                 :data-length diff-length
                 :data-start current-pos} 
                diff-val)))))

