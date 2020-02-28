set term png
set output 'result_EnglishAnalyzer.png'
set title 'EnglishAnalyzer'
set ylabel 'Precision'
set xlabel 'Recall'
set xrange [0:1]
set yrange [0:1]
set xtics 0,.2,1
set ytics 0,.2,1

plot 'result_EnglishAnalyzer_BM25Similarity.dat' title 'BM25' with lines, 'result_EnglishAnalyzer_ClassicSimilarity.dat' title 'Classic' with lines, 'result_EnglishAnalyzer_LMDirichletSimilarity.dat' title 'LMDirichlet' with lines
