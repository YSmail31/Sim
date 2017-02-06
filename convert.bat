e:
cd "E:\NetBeansProjects\Sim"
latex trace.tex	
dvips trace.ps trace
ps2pdf trace.ps
"C:/Program Files/Adobe/Reader 11.0/Reader/AcroRd32.exe" trace.pdf