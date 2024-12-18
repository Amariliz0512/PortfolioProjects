/*
Covid 19 Data Exploration 

Skills used: Joins, CTE's, Temp Tables, Windows Functions, Aggregate Functions, Creating Views, Converting Data Types

*/

SELECT * 
FROM 
	PorfolioProject.dbo.CovidDeaths
ORDER BY 
	location, 
	date;


--Select Data that we are going to be using

SELECT 
	Location, 
	date, 
	total_cases, 
	new_cases, 
	total_deaths, 
	population
FROM 
	PorfolioProject.dbo.CovidDeaths
ORDER BY 
	location, 
	date;


--Updating all 0's to null so it does not give me an error when I try to divide.

Update PorfolioProject.dbo.CovidDeaths
SET 
	total_cases = CASE WHEN total_cases = 0 THEN NULL ELSE total_cases END,
	new_cases = CASE WHEN new_cases = 0 THEN NULL ELSE new_cases END,
	total_deaths = CASE WHEN total_deaths = 0 THEN NULL ELSE total_deaths END;


--Looking at Total Cases vs Total Deaths
--Shows the likelihood of dying if you contract covid in your country

SELECT 
	Location, 
	date, 
	total_cases, 
	total_deaths, 
	(total_deaths/total_cases)*100 AS DeathPercentage
FROM 
	PorfolioProject.dbo.CovidDeaths
WHERE 
	location like '%states%'
ORDER BY 
	location, 
	date;

--Total Cases vs Population
-- Percentage of population infeted with covid

SELECT 
	Location, 
	date, 
	Population, 
	total_cases, 
	(total_cases/population) * 100 AS PercentagePopulationInfected
FROM 
	PorfolioProject.dbo.CovidDeaths
--WHERE 
--	continent is not null
ORDER BY 
	location, 
	date;


--Countries with highest infection rate compared to Population

SELECT 
	Location, 
	Population, 
	MAX(total_cases) as HighestInfectionCount, 
	Max((total_cases/population)) * 100 AS PercentagePopulationInfected
FROM 
	PorfolioProject.dbo.CovidDeaths
WHERE 
	continent is not null
GROUP BY 
	Location, 
	Population
ORDER BY PercentagePopulationInfected desc


--Countries with highest death count per population

SELECT 
	Location, 
	MAX(cast(total_deaths as int)) AS TotalDeathCount 
FROM 
	PorfolioProject.dbo.CovidDeaths
WHERE 
	continent is not null
GROUP BY 
	Location
ORDER BY 
	TotalDeathCount desc


--Continents with the highest death counts per population

SELECT 
	continent, 
	MAX(cast(total_deaths as int)) AS TotalDeathCount 
FROM 
	PorfolioProject.dbo.CovidDeaths
WHERE 
	continent is not null
GROUP BY 
	continent
ORDER BY 
	TotalDeathCount desc


--Global Numbers

SELECT 
SUM(cast(new_cases as int)) AS Total_Cases, 
SUM(cast(new_deaths as int)) AS Total_Deaths, 
SUM(cast(new_deaths AS INT)) / NULLIF(SUM(CAST(new_cases AS INT)), 0) * 100 AS DeathPercentage  
FROM 
	PorfolioProject.dbo.CovidDeaths
WHERE 
	continent IS NOT NULL AND new_cases > 0
ORDER BY 1,2


--Total Population Vs Vaccinations
-- Shows Percentage of Population that has recieved at least one Covid Vaccine

SELECT 
	dea.continent, 
	dea.location, 
	dea.date, 
	dea.population, 
	vac.new_vaccinations,
	SUM(CAST(vac.new_vaccinations AS BIGINT)) 
		OVER (PARTITION BY dea.location ORDER BY dea.date) AS RollingPeopleVaccinated
FROM 
	PorfolioProject.dbo.CovidDeaths dea 
JOIN 
	PorfolioProject.dbo.CovidVaccinations vac
	ON dea.location = vac.location 
	AND dea.date = vac.date
WHERE 
	dea.continent IS NOT NULL
ORDER BY 
	dea.location, dea.date;


--Using CTE to perform Calculation on Partition By in previous query

WITH PopvsVac (Continent, Location, Date, Population, New_Vaccinations, RollingPeopleVaccinated) 
AS
(
SELECT 
	dea.continent, 
	dea.location, 
	dea.date, 
	dea.population, 
	vac.new_vaccinations,
	SUM(CAST(vac.new_vaccinations AS BIGINT)) 
		OVER (PARTITION BY dea.location ORDER BY dea.date) AS RollingPeopleVaccinated
FROM 
	PorfolioProject.dbo.CovidDeaths dea 
JOIN PorfolioProject.dbo.CovidVaccinations vac
	ON dea.location = vac.location 	
	AND dea.date = vac.date
WHERE 
	dea.continent IS NOT NULL
)
SELECT*, (RollingPeopleVaccinated/Population) * 100
FROM PopvsVac


-- Using Temp Table to perform Calculation on Partition By in previous query

DROP TABLE IF EXISTS #PercentPopulationVaccinated
CREATE TABLE #PercentPopulationVaccinated
(
Contienent nvarchar(255),
Location nvarchar (255),
Date datetime,
Population numeric,
New_vaccinations numeric,
RollingPeopleVaccinated numeric
)

INSERT INTO #PercentPopulationVaccinated
SELECT 
	dea.continent, 
	dea.location, 
	dea.date, 
	dea.population, 
	vac.new_vaccinations,
	SUM(CAST(vac.new_vaccinations AS BIGINT)) 
		OVER (PARTITION BY dea.location ORDER BY dea.date) AS RollingPeopleVaccinated
FROM 
	PorfolioProject.dbo.CovidDeaths dea 
JOIN 
	PorfolioProject.dbo.CovidVaccinations vac
	ON dea.location = vac.location
	AND dea.date = vac.date
WHERE dea.continent IS NOT NULL

SELECT*, (RollingPeopleVaccinated/population) * 100
FROM #PercentPopulationVaccinated


--Creating View to store date for later visualizations

CREATE VIEW PercentPopulationVaccinated AS
SELECT 
	dea.continent, 
	dea.location, 
	dea.date, 
	dea.population, 
	vac.new_vaccinations,
	SUM(CAST(vac.new_vaccinations AS BIGINT)) 
		OVER (PARTITION BY dea.location ORDER BY dea.date) AS RollingPeopleVaccinated
FROM 
	PorfolioProject.dbo.CovidDeaths dea
JOIN 
	PorfolioProject.dbo.CovidVaccinations vac
	ON dea.location = vac.location
	AND dea.date = vac.date
WHERE 
	dea.continent IS NOT NULL

