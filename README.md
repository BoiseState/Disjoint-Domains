# Disjoint-Domains
This repository contains data for "Exploiting Domain and Program Structure to Synthesize Efficient and Precise Data Flow Analyses"

There are three top-level directories
artifacts -- contains 19 classes used in the experiments
domain -- contains 27 files with domain descriptions

Mapping from the domain files ids to their descriptions:
dom4 - small BL 1
dom41 - small BL 2
dom42 - small BL 3
dom5 - small DD 1
dom51 - small DD 2
dom52 - small DD 3
dom5_4 - small DD_ 1
dom51_41 - small DD_ 2
dom52_42 - small DD_ 3 

dom6 - medium BL 1
dom61 - medium BL 2
dom62 - medium BL 3
dom7 - medium DD 1
dom71 - medium DD 2
dom72 - medium DD 3
dom7_6 - medium DD_ 1
dom71_61 - medium DD_ 2
dom72_62 - medium DD_ 3

dom8 - large BL 1
dom81 - large BL 2
dom82 - large BL 3
dom9 - large DD 1
dom91 - large DD 2
dom92 - large DD 3
dom9_8 - large DD_ 1
dom91_81 - large DD_ 2
dom92_82 - large DD_ 3

experiment -- contains two folders accuracy and time with the corresponding experiment data
experiment/accuracy -- contains the results of analyses as state description in smt2 format. Each file contains data for all methods for corresponding domain. sN suffix means no black transfer function and sY means that block transfer function was enabled.
experiment/time -- timeData1 contains running time(x3) per method for small domains, timeData2 for medium domains and timeData3 for large domains
