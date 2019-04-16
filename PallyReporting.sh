while read line; do
echo "url to test : $line"

pa11y -r html --standard WCAG2AA -i "notice;warning" -i "WCAG2AA.Principle1.Guideline1_1.1_1_1.H67.2;WCAG2AA.Principle1.Guideline1_1.1_1_1.G94.Image;WCAG2AA.Principle1.Guideline1_1.1_1_1.H24;WCAG2AA.Principle1.Guideline1_1.1_1_1.H24.2;WCAG2AA.Principle1.Guideline1_1.1_1_1.G73,G74;WCAG2AA.Principle1.Guideline1_1.1_1_1.H2.EG5;WCAG2AA.Principle1.Guideline1_1.1_1_1.H2.EG3;WCAG2AA.Principle1.Guideline1_1.1_1_1.G94,G92.Object;WCAG2AA.Principle1.Guideline1_2.1_2_4.G9,G87,G93;WCAG2AA.Principle1.Guideline1_2.1_2_2.G87,G93;WCAG2AA.Principle1.Guideline1_2.1_2_5.G78,G173,G8;WCAG2AA.Principle1.Guideline1_3.1_3_1.H48.1;WCAG2AA.Principle1.Guideline1_3.1_3_1.H48.2;WCAG2AA.Principle1.Guideline1_3.1_3_2.G57;WCAG2AA.Principle1.Guideline1_4.1_4_2.F23;WCAG2AA.Principle1.Guideline1_4.1_4_1.G14,G182;WCAG2AA.Principle1.Guideline1_4.1_4_3.G18.Fail;WCAG2AA.Principle1.Guideline1_4.1_4_3.G145.Fail;WCAG2AA.Principle1.Guideline1_4.1_4_3.G18;WCAG2AA.Principle1.Guideline1_4.1_4_3.G145;WCAG2AA.Principle1.Guideline1_4.1_4_3.F24.BGColour;WCAG2AA.Principle1.Guideline1_4.1_4_3.F24.FGColour;WCAG2AA.Principle1.Guideline1_4.1_4_4.G142;WCAG2AA.Principle1.Guideline1_4.1_4_5.G140,C22,C30.AALevel;WCAG2AA.Principle3.Guideline3_2.3_2_2.H32.2;WCAG2AA.Principle3.Guideline3_2.3_2_3.G61;WCAG2AA.Principle3.Guideline3_2.3_2_4.G197;WCAG2AA.Principle3.Guideline3_3.3_3_1.G83,G84,G85;WCAG2AA.Principle4.Guideline4_1.4_1_2.H91.InputRange.Name;WCAG2AA.Principle4.Guideline4_1.4_1_2.H91.A.NoContent" "$line"


done < PallyUrls.txt

