set term png
set output 'result_SimpleAnalyzer.png'
set title 'SimpleAnalyzer'
set ylabel 'Precision'
set xlabel 'Recall'
set xrange [0:1]
set yrange [0:1]
set xtics 0,.2,1
set ytics 0,.2,1

plot 'result_SimpleAnalyzer_BM25Similarity.dat' title 'BM25' with lines, 'result_SimpleAnalyzer_ClassicSimilarity.dat' title 'Classic' with lines, 'result_SimpleAnalyzer_LMDirichletSimilarity.dat' title 'LMDirichlet' with lines
