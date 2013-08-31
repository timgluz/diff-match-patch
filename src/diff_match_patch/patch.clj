(ns diff-match-patch.core)

(defn patch-texts [^:String txt1 ^:String txt2]
  "Given two texts, returns an array of patch objects."
  (.patch_make *diffor* txt1 txt2))

(defn patch-diffs [diffs]
  "Given already computed diffs, returns an array of patch objects"
  (.patch_make *diffor* diffs))

(defn patch-text-diffs [^:String txt1, diffs]
  "Given old text and already computed differences, returns an array of patch objects."
  (.patch_make *diffor* diffs))

(defn apply-patch [patches, ^:String old-text]
  "Merge a set of patches onto the text. Returns a patched text,
  as well as an array of true/false values indicating which patches were applied"
  (.patch_apply *diffor* patches old-text))

(defn export-patch [patches]
  "Reduces an array of patch objects to a block of text which looks extremely similar
  to the standard GNU diff/patch format. This text may stored or transmitted."
  (.patch_toText *diffor* patches))

(defn import-patch [^:String patch-txt]
  "Parses a block of text and return an array of patch objects"
  (.patch_fromText *diffor* patch-txt))
