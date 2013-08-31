(ns diff-match-patch.core)

(defn match [^:String text, ^:String pattern, ^:Integer loc]
  "Locate the best instance of pattern in text near loc."
  (.match_main *diffor* text pattern loc))

