# diff-match-patch
Clojure wrapper for Java's [google-diff-match](https://code.google.com/p/google-diff-match-patch/wiki/API) library.

## Install
[diff-match-patch "0.1.0-SNAPSHOT"]

## Usage

**(require '[diff-match-patch :as dmp])**

*ps: function name may not match 1-to-1 with original Java names*

**Using diff functionalities**

```
;; calc diffs

repl> (def diffs (dmp/calc-diffs "Text1" "Text2"))
(#<Diff Diff(EQUAL,"text")> #<Diff Diff(DELETE,"1")> #<Diff Diff(INSERT,"2")>)

;;clean up diffs
repl> (dmp/cleanup! diffs :method :semantic) ;;[supported methods :semantic, :lossless, :efficiency :merge]

;; calculate some metrics of calculated differences
repl> (dmp/levenshtein diffs)
1
repl> (dmp/x-index diffs 5) ;; search text2 position in text1 nearby position 5

;; public utility functions
repl> (dmp/common-prefix "123" "1") ; 1
repl> (dmp/common-suffix "123" "23"); 2

repl> (dmp/to-source-text diffs) ;; "Text1"
repl> (dmp/to-destination-text diffs) ;; "Text2"
repl> (def delta-txt (dmp/to-delta diffs))
"=4\t-1\t+2"
repl> (dmp/from-delta "Text1" delta-txt) ;; restore text2 by using original text and delta-string
(#<Diff Diff(EQUAL,"Text")> #<Diff Diff(DELETE,"1")> #<Diff Diff(INSERT,"2")>)

repl> (dmp/pretty-html diffs)
"<span>Text</span><del style=\"background:#ffe6e6;\">1</del><ins style=\"background:#e6ffe6;\">2</ins>"
repl> (dmp/to-hiccup diffs)
([:span {:class "equal", :data-length 4, :data-start 0} "Text"] [:span {:class "delete", :data-length 1, :data-start 4} "1"] [:span {:class "insert", :data-length 1, :data-start 4} "2"])

```

## License

Copyright © 2013 

Distributed under the Eclipse Public License, the same as Clojure.
