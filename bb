import requests
from bs4 import BeautifulSoup
import csv

# Function to scrape election results
def scrape_election_results(url):
    # Send HTTP request
    response = requests.get(url)
    
    if response.status_code == 200:
        # Parse HTML content
        soup = BeautifulSoup(response.text, 'html.parser')
        
        # Find relevant tables or sections containing data
        tables = soup.find_all('table')
        
        # Example: Extracting constituency-wise results
        results_data = []
        
        for table in tables:
            rows = table.find_all('tr')
            
            for row in rows:
                columns = row.find_all('td')
                
                if len(columns) > 1:  # Ensure it's a data row
                    constituency = columns[0].text.strip()
                    candidate_name = columns[1].text.strip()
                    votes = columns[2].text.strip()
                    
                    # Store data in a list (or you can directly write to CSV/database)
                    results_data.append({
                        'Constituency': constituency,
                        'Candidate Name': candidate_name,
                        'Votes': votes
                    })
        
        return results_data
    else:
        print(f"Failed to retrieve data. Status code: {response.status_code}")
        return None

# URL of the ECI results page
url = 'https://results.eci.gov.in'

# Call the function to scrape data
election_results = scrape_election_results(url)

# Example: Write data to CSV file
if election_results:
    with open('election_results.csv', 'w', newline='', encoding='utf-8') as csvfile:
        fieldnames = ['Constituency', 'Candidate Name', 'Votes']
        writer = csv.DictWriter(csvfile, fieldnames=fieldnames)
        
        writer.writeheader()
        for result in election_results:
            writer.writerow(result)
    
    print("Data extraction and writing to CSV completed successfully.")
else:
    print("Data extraction failed. Please check the URL or try again later.")

