## Example usage

```bash
python calc_games.py ../group-1.txt --latex
for sheet in scoresheet_*.tex
do 
    echo -n "$sheet... "
    xelatex $sheet >/dev/null
    echo "done"
done
pdftk scoresheet_*.pdf cat output group-1.pdf
# rm scoresheet_*
```
