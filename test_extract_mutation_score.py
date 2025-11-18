#!/usr/bin/env python3
"""Test script to verify extract_mutation_score.py works correctly"""

import subprocess
import sys
import os

def test_extract_mutation_score():
    """Test the extract_mutation_score.py script"""
    script_path = ".github/scripts/extract_mutation_score.py"
    test_report_path = "core/target/pit-reports"
    
    # Check if the report directory exists
    if not os.path.exists(test_report_path):
        print(f"[ERROR] Test report directory not found: {test_report_path}")
        print("   Please run pitest first to generate reports.")
        return False
    
    # Check if index.html exists
    index_html = os.path.join(test_report_path, "index.html")
    if not os.path.exists(index_html):
        print(f"[ERROR] index.html not found in {test_report_path}")
        return False
    
    # Read the expected value from the HTML file manually
    print(f"[INFO] Reading expected value from {index_html}...")
    with open(index_html, encoding="utf-8") as f:
        html = f.read()
    
    # Extract Mutation Coverage manually to verify
    import re
    summary_section = re.search(
        r"<h[23]>(?:Project|Package) Summary</h[23]>.*?<th>Mutation Coverage</th>.*?<tbody>.*?<tr>.*?</tr>",
        html,
        re.DOTALL
    )
    if summary_section:
        all_percentages = re.findall(r"<td>\s*(\d+(?:\.\d+)?)%", summary_section.group(0))
        if len(all_percentages) >= 2:
            expected_mutation_score = all_percentages[1]
            print(f"[OK] Expected Mutation Coverage: {expected_mutation_score}%")
        else:
            print("[ERROR] Could not find expected mutation score in HTML")
            return False
    else:
        print("[ERROR] Could not find summary section in HTML")
        return False
    
    # Run the script
    print(f"[INFO] Running script: {script_path} {test_report_path}")
    try:
        result = subprocess.run(
            [sys.executable, script_path, test_report_path],
            capture_output=True,
            text=True,
            check=True
        )
        actual_score = result.stdout.strip()
        print(f"[INFO] Actual Mutation Coverage extracted: {actual_score}%")
        
        # Compare
        if actual_score == expected_mutation_score:
            print(f"[SUCCESS] Script correctly extracted {actual_score}%")
            return True
        else:
            print(f"[FAILURE] Expected {expected_mutation_score}%, got {actual_score}%")
            return False
    except subprocess.CalledProcessError as e:
        print(f"[ERROR] Script failed with error:")
        print(f"   Return code: {e.returncode}")
        print(f"   stdout: {e.stdout}")
        print(f"   stderr: {e.stderr}")
        return False
    except Exception as e:
        print(f"[ERROR] Error running script: {e}")
        return False

if __name__ == "__main__":
    success = test_extract_mutation_score()
    sys.exit(0 if success else 1)

