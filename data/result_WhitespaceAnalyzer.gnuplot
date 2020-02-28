set term png
set output 'result_WhitespaceAnalyzer.png'
set title 'WhitespaceAnalyzer'
set ylabel 'Precision'
set xlabel 'Recall'
set xrange [0:1]
set yrange [0:1]
set xtics 0,.2,1
set ytics 0,.2,1

plot 'result_WhitespaceAnalyzer_BM25Similarity.dat' title 'BM25' with lines, 'result_WhitespaceAnalyzer_ClassicSimilarity.dat' title 'Classic' with lines, 'result_WhitespaceAnalyzer_LMDirichletSimilarity.dat' title 'LMDirichlet' with lines
