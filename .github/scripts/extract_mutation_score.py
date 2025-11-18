#!/usr/bin/env python3

import os
import re
import sys


def main():
    if len(sys.argv) < 2:
        print("Usage: python extract_mutation_score.py <report_root>", file=sys.stderr)
        sys.exit(1)

    report_root = sys.argv[1]
    if not os.path.isdir(report_root):
        print(f"Report folder not found: {report_root}", file=sys.stderr)
        sys.exit(1)

    # Check if index.html exists in the root directory (Project Summary)
    index_path = os.path.join(report_root, "index.html")
    if not os.path.exists(index_path):
        # Fallback: look in subdirectories if root index.html doesn't exist
        sub_dirs = sorted(
            [name for name in os.listdir(report_root) if os.path.isdir(os.path.join(report_root, name))]
        )
        if not sub_dirs:
            print("No PIT reports were generated.", file=sys.stderr)
            sys.exit(1)
        index_path = os.path.join(report_root, sub_dirs[-1], "index.html")
        if not os.path.exists(index_path):
            print(f"Cannot find index.html in {index_path}", file=sys.stderr)
            sys.exit(1)

    with open(index_path, encoding="utf-8") as handle:
        html = handle.read()

    # Extract Mutation Coverage from the summary table (Project Summary or Package Summary)
    # Structure: <h3>Project Summary</h3> or <h3>Package Summary</h3> <table> 
    #           <thead> <th>Number of Classes</th> <th>Line Coverage</th> <th>Mutation Coverage</th> </thead>
    #           <tbody> <tr> <td>N</td> <td>XX%</td> (Line) <td>YY%</td> (Mutation) </tr> </tbody>
    # We need the third <td> value in the tbody row (Mutation Coverage is the 3rd column)
    
    # Find a summary section (Project Summary or Package Summary) that has Mutation Coverage
    summary_section = re.search(
        r"<h[23]>(?:Project|Package) Summary</h[23]>.*?<th>Mutation Coverage</th>.*?<tbody>.*?<tr>.*?</tr>",
        html,
        re.DOTALL
    )
    if not summary_section:
        print("Could not find summary table with Mutation Coverage.", file=sys.stderr)
        sys.exit(1)
    
    # Extract the Mutation Coverage value - it's the third <td> in the tbody row
    # Pattern: <td>N</td> (Number of Classes) <td>XX%</td> (Line Coverage) <td>YY%</td> (Mutation Coverage)
    # Find all percentages in <td> tags within the tbody row - the second one is Mutation Coverage
    # (First is Line Coverage, second is Mutation Coverage, third is Test Strength)
    all_percentages = re.findall(r"<td>\s*(\d+(?:\.\d+)?)%", summary_section.group(0))
    if len(all_percentages) >= 2:
        # First percentage is Line Coverage, second is Mutation Coverage
        mutation_score = all_percentages[1]
    else:
        print(f"Could not read the mutation coverage score. Found {len(all_percentages)} percentages, expected at least 2.", file=sys.stderr)
        sys.exit(1)

    print(mutation_score)


if __name__ == "__main__":
    main()
