local MapGenerator = {}
local TERRAIN = workspace.Terrain

local BIOME_CONFIG = {
	Nether = { color = Color3.fromRGB(180, 40, 40), material = Enum.Material.Slate, height = 0.2 },
	Swamp = { color = Color3.fromRGB(55, 80, 40), material = Enum.Material.Mud, height = 0.35 },
	Badlands = { color = Color3.fromRGB(160, 110, 60), material = Enum.Material.Sandstone, height = 0.5 },
	Grasslands = { color = Color3.fromRGB(90, 150, 60), material = Enum.Material.Grass, height = 0.45 },
	Mountains = { color = Color3.fromRGB(130, 130, 130), material = Enum.Material.Slate, height = 0.85 },
	Desert = { color = Color3.fromRGB(200, 170, 100), material = Enum.Material.Sand, height = 0.3 },
	Snowy = { color = Color3.fromRGB(220, 230, 240), material = Enum.Material.Ice, height = 0.4 },
	Tundra = { color = Color3.fromRGB(180, 190, 200), material = Enum.Material.Snow, height = 0.3 },
	Ocean = { color = Color3.fromRGB(30, 80, 140), material = Enum.Material.Water, height = 0.1 },
	Forest = { color = Color3.fromRGB(40, 110, 30), material = Enum.Material.Grass, height = 0.5 },
	Jungle = { color = Color3.fromRGB(35, 100, 25), material = Enum.Material.Grass, height = 0.55 },
}

local function createPart(size, cframe, color, material, parent)
	local p = Instance.new("Part")
	p.Size = size
	p.CFrame = cframe
	p.Color = color
	p.Material = material or Enum.Material.Stone
	p.Anchored = true
	p.TopSurface = Enum.SurfaceType.Smooth
	p.BottomSurface = Enum.SurfaceType.Smooth
	p.Parent = parent
	return p
end

local function createWedge(size, cframe, color, material, parent)
	local w = Instance.new("WedgePart")
	w.Size = size
	w.CFrame = cframe
	w.Color = color
	w.Material = material or Enum.Material.Stone
	w.Anchored = true
	w.Parent = parent
	return w
end

local function createCylinder(size, cframe, color, material, parent)
	local c = Instance.new("CylinderPart")
	c.Size = size
	c.CFrame = cframe
	c.Color = color
	c.Material = material or Enum.Material.Stone
	c.Anchored = true
	c.Parent = parent
	return c
end

--- STRUCTURE BUILDERS ---

local function buildPyramid(origin, scale, color, parent)
	local s = scale or 1
	local h = 4 * s
	local folder = Instance.new("Folder")
	folder.Name = "Pyramid"
	folder.Parent = parent

	for i = 1, 4 do
		local layerSize = (5 - i) * s * 2
		if layerSize <= 0 then break end
		local p = createPart(
			Vector3.new(layerSize, h / 4, layerSize),
			CFrame.new(origin.X, origin.Y + (h / 4 * i) - (h / 8), origin.Z),
			color, Enum.Material.Sandstone, folder
		)
		p.Size = Vector3.new(layerSize, h / 4, layerSize)
	end

	local entrance = createPart(
		Vector3.new(2 * s, 3 * s, 1 * s),
		CFrame.new(origin.X, origin.Y + 1.5 * s, origin.Z + 2.5 * s),
		Color3.fromRGB(30, 20, 10), Enum.Material.Wood, folder
	)
	entrance.Name = "Entrance"
end

local function buildGreekTemple(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "GreekTemple"
	folder.Parent = parent

	local floor = createPart(
		Vector3.new(12 * s, 0.5 * s, 8 * s),
		CFrame.new(origin.X, origin.Y + 0.25 * s, origin.Z),
		color, Enum.Material.Sandstone, folder
	)
	floor.Name = "Floor"

	for i = -4, 4, 2 do
		for j = -3, 3, 2 do
			local pillar = createCylinder(
				Vector3.new(1 * s, 3 * s, 1 * s),
				CFrame.new(origin.X + i * s, origin.Y + 1.75 * s, origin.Z + j * s),
				Color3.fromRGB(200, 190, 170), Enum.Material.Limestone, folder
			)
			pillar.Name = "Pillar"
		end
	end

	local roof = createPart(
		Vector3.new(14 * s, 0.5 * s, 10 * s),
		CFrame.new(origin.X, origin.Y + 3.5 * s, origin.Z),
		color, Enum.Material.Sandstone, folder
	)
	roof.Name = "Roof"

	local pediment = createWedge(
		Vector3.new(14 * s, 1.5 * s, 0.5 * s),
		CFrame.new(origin.X, origin.Y + 4.5 * s, origin.Z - 5 * s),
		color, Enum.Material.Sandstone, folder
	)
	pediment.Name = "Pediment"
end

local function buildJapaneseShrine(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "JapaneseShrine"
	folder.Parent = parent

	local base = createPart(
		Vector3.new(6 * s, 0.5 * s, 5 * s),
		CFrame.new(origin.X, origin.Y + 0.25 * s, origin.Z),
		Color3.fromRGB(150, 100, 50), Enum.Material.Wood, folder
	)
	base.Name = "Base"

	for i = -2, 2, 2 do
		local pillar = createPart(
			Vector3.new(0.5 * s, 2.5 * s, 0.5 * s),
			CFrame.new(origin.X + i * s, origin.Y + 1.5 * s, origin.Z),
			Color3.fromRGB(180, 60, 60), Enum.Material.Wood, folder
		)
		pillar.Name = "Pillar"
	end

	local roofBase = createPart(
		Vector3.new(7 * s, 0.4 * s, 6 * s),
		CFrame.new(origin.X, origin.Y + 3.2 * s, origin.Z),
		Color3.fromRGB(120, 40, 40), Enum.Material.Wood, folder
	)
	roofBase.Name = "RoofBase"

	local roof = createWedge(
		Vector3.new(8 * s, 2 * s, 6 * s),
		CFrame.new(origin.X, origin.Y + 4.4 * s, origin.Z),
		Color3.fromRGB(120, 40, 40), Enum.Material.Wood, folder
	)
	roof.Name = "RoofTop"

	local gate = createPart(
		Vector3.new(3 * s, 3 * s, 0.5 * s),
		CFrame.new(origin.X, origin.Y + 1.75 * s, origin.Z - 3 * s),
		Color3.fromRGB(180, 60, 60), Enum.Material.Wood, folder
	)
	gate.Name = "ToriiGate"
end

local function buildLabirynthFragment(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "Labyrinth"
	folder.Parent = parent

	local layout = {
		{ 0, 0, 6, 1 }, { 0, 2, 6, 1 }, { 0, -2, 6, 1 },
		{ 3, 0, 1, 6 }, { -3, 0, 1, 6 },
		{ 4, 2, 3, 1 }, { -4, -2, 3, 1 },
	}

	for _, w in ipairs(layout) do
		local wall = createPart(
			Vector3.new(w[3] * s, 3 * s, w[4] * s),
			CFrame.new(origin.X + w[1] * s, origin.Y + 1.5 * s, origin.Z + w[2] * s),
			color, Enum.Material.Limestone, folder
		)
		wall.Name = "Wall"
	end
end

local function buildNetherFortress(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "NetherFortress"
	folder.Parent = parent

	local base = createPart(
		Vector3.new(8 * s, 0.5 * s, 8 * s),
		CFrame.new(origin.X, origin.Y + 0.25 * s, origin.Z),
		Color3.fromRGB(60, 10, 10), Enum.Material.Slate, folder
	)
	base.Name = "Base"

	for i = -3, 3, 2 do
		local pillar = createPart(
			Vector3.new(0.6 * s, 4 * s, 0.6 * s),
			CFrame.new(origin.X + i * s, origin.Y + 2 * s, origin.Z),
			Color3.fromRGB(40, 5, 5), Enum.Material.Slate, folder
		)
		pillar.Name = "Spike"
	end

	for i = -2, 2, 2 do
		local spike = createWedge(
			Vector3.new(0.8 * s, 1.5 * s, 0.8 * s),
			CFrame.new(origin.X + i * s, origin.Y + 5 * s, origin.Z + 2 * s),
			Color3.fromRGB(40, 5, 5), Enum.Material.Slate, folder
		)
		spike.Name = "Spike"
	end

	local lava = createPart(
		Vector3.new(3 * s, 0.2 * s, 3 * s),
		CFrame.new(origin.X + 2 * s, origin.Y + 0.4 * s, origin.Z + 2 * s),
		Color3.fromRGB(255, 100, 0), Enum.Material.Neon, folder
	)
	lava.Name = "LavaPool"
	lava.MaterialVariant = "Lava"

	local fire = Instance.new("Fire")
	fire.Parent = lava
end

local function buildSwampHut(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "SwampHut"
	folder.Parent = parent

	for i = -1, 1, 2 do
		for j = -1, 1, 2 do
			local leg = createPart(
				Vector3.new(0.3 * s, 2 * s, 0.3 * s),
				CFrame.new(origin.X + i * 1.5 * s, origin.Y + s, origin.Z + j * 1.5 * s),
				Color3.fromRGB(100, 60, 20), Enum.Material.Wood, folder
			)
			leg.Name = "Stilt"
		end
	end

	local floor = createPart(
		Vector3.new(4 * s, 0.3 * s, 4 * s),
		CFrame.new(origin.X, origin.Y + 2.15 * s, origin.Z),
		Color3.fromRGB(120, 70, 20), Enum.Material.Wood, folder
	)
	floor.Name = "Floor"

	for i = -1, 1, 2 do
		local wall = createPart(
			Vector3.new(0.3 * s, 2 * s, 4 * s),
			CFrame.new(origin.X + i * 1.85 * s, origin.Y + 3.15 * s, origin.Z),
			Color3.fromRGB(100, 60, 20), Enum.Material.Wood, folder
		)
		wall.Name = "Wall"
	end

	local roof = createWedge(
		Vector3.new(5 * s, 2 * s, 5 * s),
		CFrame.new(origin.X, origin.Y + 4.5 * s, origin.Z),
		Color3.fromRGB(80, 90, 30), Enum.Material.Wood, folder
	)
	roof.Name = "Roof"
end

local function buildStoneCircle(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "StoneCircle"
	folder.Parent = parent

	local center = createPart(
		Vector3.new(1 * s, 0.3 * s, 1 * s),
		CFrame.new(origin.X, origin.Y + 0.15 * s, origin.Z),
		Color3.fromRGB(80, 80, 90), Enum.Material.Slate, folder
	)
	center.Name = "Altar"

	for i = 0, 7 do
		local angle = (i / 8) * math.pi * 2
		local rx = math.cos(angle) * 5 * s
		local rz = math.sin(angle) * 5 * s
		local stone = createPart(
			Vector3.new(0.8 * s, 2.5 * s, 0.5 * s),
			CFrame.new(origin.X + rx, origin.Y + 1.25 * s, origin.Z + rz),
			Color3.fromRGB(100, 100, 110), Enum.Material.Limestone, folder
		)
		stone.Name = "StandingStone"
	end
end

local function buildIceCave(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "IceCave"
	folder.Parent = parent

	local arch = createPart(
		Vector3.new(8 * s, 5 * s, 3 * s),
		CFrame.new(origin.X, origin.Y + 2.5 * s, origin.Z),
		Color3.fromRGB(180, 220, 240), Enum.Material.Ice, folder
	)
	arch.Name = "Arch"
	arch.Transparency = 0.3

	for i = -3, 3, 1.5 do
		local icicle = createWedge(
			Vector3.new(0.5 * s, 1.5 * s, 0.5 * s),
			CFrame.new(origin.X + i * s, origin.Y + 5 * s, origin.Z + 2 * s),
			Color3.fromRGB(200, 230, 250), Enum.Material.Ice, folder
		)
		icicle.Transparency = 0.2
		icicle.Name = "Icicle"
	end
end

local function buildNest(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "Nest"
	folder.Parent = parent

	local base = createPart(
		Vector3.new(5 * s, 0.5 * s, 5 * s),
		CFrame.new(origin.X, origin.Y + 0.25 * s, origin.Z),
		Color3.fromRGB(90, 60, 30), Enum.Material.Wood, folder
	)
	base.Name = "Base"

	for i = 1, 12 do
		local angle = (i / 12) * math.pi * 2
		local rx = math.cos(angle) * 2.5 * s
		local rz = math.sin(angle) * 2.5 * s
		local twig = createPart(
			Vector3.new(0.3 * s, 0.3 * s, 1 * s),
			CFrame.new(origin.X + rx, origin.Y + 0.5 * s, origin.Z + rz) * CFrame.Angles(0, angle, math.rad(15)),
			Color3.fromRGB(120, 80, 40), Enum.Material.Wood, folder
		)
		twig.Name = "Twig"
	end
end

local function buildObelisk(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "Obelisk"
	folder.Parent = parent

	local base = createPart(
		Vector3.new(2 * s, 0.5 * s, 2 * s),
		CFrame.new(origin.X, origin.Y + 0.25 * s, origin.Z),
		color, Enum.Material.Sandstone, folder
	)
	base.Name = "Base"

	local shaft = createPart(
		Vector3.new(1 * s, 5 * s, 1 * s),
		CFrame.new(origin.X, origin.Y + 3 * s, origin.Z),
		Color3.fromRGB(180, 150, 100), Enum.Material.Sandstone, folder
	)
	shaft.Name = "Shaft"

	local tip = createWedge(
		Vector3.new(1 * s, 1.5 * s, 1 * s),
		CFrame.new(origin.X, origin.Y + 6.25 * s, origin.Z),
		Color3.fromRGB(200, 170, 110), Enum.Material.Sandstone, folder
	)
	tip.Name = "Tip"
end

local function buildUnderwaterRuins(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "UnderwaterRuins"
	folder.Parent = parent

	local floor = createPart(
		Vector3.new(10 * s, 0.5 * s, 10 * s),
		CFrame.new(origin.X, origin.Y + 0.25 * s, origin.Z),
		Color3.fromRGB(60, 80, 70), Enum.Material.Limestone, folder
	)
	floor.Name = "Floor"
	floor.Transparency = 0.3

	for i = -3, 3, 2 do
		for j = -3, 3, 2 do
			if math.random() < 0.6 then
				local column = createPart(
					Vector3.new(0.8 * s, 2 * s, 0.8 * s),
					CFrame.new(origin.X + i * s, origin.Y + 1.25 * s, origin.Z + j * s),
					Color3.fromRGB(70, 90, 80), Enum.Material.Limestone, folder
				)
				column.Transparency = 0.2
				column.Name = "Column"
			end
		end
	end
end

local function buildMayanPyramid(origin, scale, color, parent)
	local s = scale or 1
	local folder = Instance.new("Folder")
	folder.Name = "MayanPyramid"
	folder.Parent = parent

	for i = 1, 3 do
		local layerSize = (7 - i * 1.5) * s
		if layerSize <= 0 then break end
		local p = createPart(
			Vector3.new(layerSize, 1.5 * s, layerSize),
			CFrame.new(origin.X, origin.Y + (1.5 * s * i) - (0.75 * s), origin.Z),
			color, Enum.Material.Sandstone, folder
		)
		p.Name = "Layer" .. i

		local stair = createPart(
			Vector3.new(layerSize * 0.4, 0.3 * s, 1.5 * s),
			CFrame.new(origin.X, origin.Y + (1.5 * s * i) - (1.05 * s), origin.Z + layerSize / 2 + 0.75 * s),
			Color3.fromRGB(150, 120, 70), Enum.Material.Sandstone, folder
		)
		stair.Name = "Stair" .. i
	end

	local temple = createPart(
		Vector3.new(3 * s, 2 * s, 2 * s),
		CFrame.new(origin.X, origin.Y + 5 * s, origin.Z),
		Color3.fromRGB(180, 150, 80), Enum.Material.Sandstone, folder
	)
	temple.Name = "TempleTop"
end

-- BIOME STRUCTURE MAP --
local BIOME_STRUCTURES = {
	Nether = {
		{ build = buildNetherFortress, weight = 1, minDist = 40 },
	},
	Swamp = {
		{ build = buildSwampHut, weight = 3, minDist = 20 },
		{ build = buildSwampHut, weight = 2, minDist = 25 },
	},
	Badlands = {
		{ build = buildLabirynthFragment, weight = 2, minDist = 25 },
		{ build = buildGreekTemple, weight = 1, minDist = 35 },
	},
	Grasslands = {
		{ build = buildStoneCircle, weight = 2, minDist = 25 },
		{ build = buildGreekTemple, weight = 1, minDist = 35 },
	},
	Mountains = {
		{ build = buildNest, weight = 3, minDist = 20 },
		{ build = buildGreekTemple, weight = 1, minDist = 35 },
	},
	Desert = {
		{ build = buildPyramid, weight = 2, minDist = 30 },
		{ build = buildObelisk, weight = 3, minDist = 20 },
	},
	Snowy = {
		{ build = buildIceCave, weight = 2, minDist = 25 },
		{ build = buildIceCave, weight = 1, minDist = 30 },
	},
	Tundra = {
		{ build = buildIceCave, weight = 2, minDist = 25 },
	},
	Ocean = {
		{ build = buildUnderwaterRuins, weight = 2, minDist = 30 },
	},
	Forest = {
		{ build = buildJapaneseShrine, weight = 2, minDist = 25 },
		{ build = buildStoneCircle, weight = 1, minDist = 30 },
	},
	Jungle = {
		{ build = buildMayanPyramid, weight = 2, minDist = 30 },
		{ build = buildMayanPyramid, weight = 1, minDist = 35 },
	},
}

local MAP_SIZE = 500
local BIOME_SIZE = 80
local PLACED = {}

local function distanceFromOrigin(x, z)
	return math.sqrt(x * x + z * z)
end

local function poissonSample(biomeX, biomeZ, minDist, count)
	local points = {}
	local attempts = 0
	while #points < count and attempts < 50 do
		local ox = math.random() * BIOME_SIZE - BIOME_SIZE / 2
		local oz = math.random() * BIOME_SIZE - BIOME_SIZE / 2
		local px = biomeX + ox
		local pz = biomeZ + oz
		local valid = true
		for _, placed in ipairs(PLACED) do
			if (Vector3.new(px - placed.X, 0, pz - placed.Z)).Magnitude < minDist then
				valid = false
				break
			end
		end
		if valid then
			table.insert(points, Vector3.new(px, 0, pz))
			table.insert(PLACED, Vector3.new(px, 0, pz))
		end
		attempts += 1
	end
	return points
end

local function getTerrainHeight(x, z)
	local y = 0
	local samplePos = Vector3.new(x, 50, z)
	local material = TERRAIN:ReadVoxel(samplePos, 1, 1, 1)
	if material == Enum.Material.Water or material == Enum.Material.Subsea then
		return -10
	end
	local _, height = TERRAIN:GetCellCenter(x, 0, z)
	do
		return height
	end
	return 0
end

local function flattenTerrain(center, radius)
	local half = radius
	for x = -half, half, 4 do
		for z = -half, half, 4 do
			local pos = Vector3.new(center.X + x, center.Y + 0.5, center.Z + z)
			TERRAIN:FillBlock(pos, Vector3.new(4, 1, 4), Enum.Material.Grass)
		end
	end
end

function MapGenerator.generate()
	print("[MapGenerator] Generating Chanequemon world...")

	-- Generate flat terrain base
	TERRAIN:Clear()
	TERRAIN:FillBlock(Vector3.new(0, -5, 0), Vector3.new(MAP_SIZE, 10, MAP_SIZE), Enum.Material.Grass)

	local worldFolder = Instance.new("Folder")
	worldFolder.Name = "ChanequemonStructures"
	worldFolder.Parent = workspace

	local biomeKeys = {}
	for k, _ in pairs(BIOME_STRUCTURES) do
		table.insert(biomeKeys, k)
	end

	local gridSize = BIOME_SIZE + 10
	local biomeIndex = 1

	for bx = -MAP_SIZE / 2 + gridSize / 2, MAP_SIZE / 2 - gridSize / 2, gridSize do
		for bz = -MAP_SIZE / 2 + gridSize / 2, MAP_SIZE / 2 - gridSize / 2, gridSize do
			local biomeName = biomeKeys[(biomeIndex - 1) % #biomeKeys + 1]
			biomeIndex += 1
			local config = BIOME_CONFIG[biomeName]
			if config then
				local biomeCenter = Vector3.new(bx, 0, bz)
				local flatPos = biomeCenter - Vector3.new(0, 0.5, 0)
				TERRAIN:FillBlock(flatPos, Vector3.new(BIOME_SIZE, 2, BIOME_SIZE), config.material)

				local colorPos = biomeCenter - Vector3.new(0, 0.5, 0)
				TERRAIN:FillBlock(colorPos, Vector3.new(BIOME_SIZE, 1, BIOME_SIZE), config.material)
			end

			-- Place biome structures
			local structures = BIOME_STRUCTURES[biomeName]
			if structures then
				for _, entry in ipairs(structures) do
					local count = entry.weight
					local points = poissonSample(bx, bz, entry.minDist, count)
					for _, pt in ipairs(points) do
						local scale = 0.8 + math.random() * 0.4
						local biomeColor = BIOME_CONFIG[biomeName] and BIOME_CONFIG[biomeName].color or Color3.fromRGB(150, 150, 150)
						entry.build(pt, scale, biomeColor, worldFolder)
					end
				end
			end
		end
	end

	print("[MapGenerator] Map generation complete!")
end

function MapGenerator.getSpawnPoint()
	return Vector3.new(0, 5, 0)
end

return MapGenerator
