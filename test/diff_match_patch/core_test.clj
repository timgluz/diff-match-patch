(ns diff-match-patch.core-test
  (:use clojure.test
        diff-match-patch.core))

(deftest diff
  (testing "creating diffs from strings"
    (let [diffs (calc-diffs "abcdef" "def")]
      (is (= 2 (count diffs)))
      (is (= "abc" (.text (first diffs))))
      (is (= "def" (.text (second diffs))))))
  (testing "cleanup! functionality"
    (let [diffs (calc-diffs "123456" "234")]
      (is (= 3 (count (cleanup! diffs))))
      (is (= 3 (count (cleanup! diffs :method :semantic))))
      (is (= 3 (count (cleanup! diffs :method :lossless))))
      (is (= 3 (count (cleanup! diffs :method :efficiency))))
      (is (= 3 (count (cleanup! diffs :method :merge))))))
  (testing "levenshtein functionality"
    (let [diffs1 (calc-diffs "abcdef" "abcdef")
          diffs2 (calc-diffs "abcdef" "12def45")]
      (is (= 0 (levenshtein diffs1)))
      (is (= 5 (levenshtein diffs2)))))
  (testing "common-prefix functionality"
    (is (= 1 (common-prefix "12345" "1")))
    (is (= 2 (common-prefix "12345" "12")))
    (is (= 3 (common-prefix "12345" "123"))))
  (testing "common-suffix functionality"
    (is (= 1 (common-suffix "123" "3")))
    (is (= 2 (common-suffix "123" "23")))
    (is (= 3 (common-suffix "123" "123"))))
  (testing "x-index functionality - find index of substring nearby loc"
    (let [diffs (calc-diffs "123456789abcdef987654321" "abcd")]
      (is (= 0 (x-index diffs 5)))
      (is (= 1 (x-index diffs 10)))
      (is (= 4 (x-index diffs 15)))))
  (testing "to-source-text functionality"
    (let [diffs (calc-diffs "abcdef" "abcdef12345")]
      (is (= "abcdef" (to-source-text diffs)))))
  (testing "to-destination-text functionality"
    (let [diffs (calc-diffs "123abcdef456" "abcdef")]
      (is (= "abcdef" (to-destination-text diffs)))))
  (testing "to-delta should return string"
    (let [diffs (calc-diffs "1234" "abcd")]
      (is (true? (string? (to-delta diffs))))))
  (testing "from-delta should return correct delta"
    (let [delta-txt (to-delta (calc-diffs "12345" "abcdef"))
          diffs (from-delta "12345" delta-txt)]
      (is (= 2 (count diffs)))
      (is (= "DELETE" (str (.operation (first diffs)))))))
  (testing "pretty-html should return html string"
    (let [diffs (calc-diffs "abc123def" "ab12de")]
      (is (string? (pretty-html diffs)))))
  (testing "to-hiccup should return list of elems"
    (let [diffs (calc-diffs "abc123def" "ab12de")]
      (is (= 6 (count (to-hiccup diffs)))))))

