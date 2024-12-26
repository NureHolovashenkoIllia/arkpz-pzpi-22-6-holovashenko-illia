package ua.nure.arkpz.task2.flameguard.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ua.nure.arkpz.task2.flameguard.dto.BuildingDto;
import ua.nure.arkpz.task2.flameguard.service.BuildingService;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing buildings.
 * Provides endpoints for CRUD operations on buildings.
 */
@RestController
@RequestMapping("/api/buildings")
@Tag(name = "Buildings", description = "Endpoints for managing buildings")
public class BuildingController {

    @Autowired
    private BuildingService buildingService;

    @Autowired
    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    /**
     * Retrieves all buildings.
     *
     * @return A list of all buildings.
     */
    @Operation(summary = "Get all buildings", description = "Retrieves a list of all buildings.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of buildings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BuildingDto.class)))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @GetMapping
    public ResponseEntity<List<BuildingDto>> getAllBuildings() {
        List<BuildingDto> buildings = buildingService.getAllBuildings();
        return ResponseEntity.ok(buildings);
    }

    /**
     * Retrieves a building by its ID.
     *
     * @param id The ID of the building.
     * @return The building with the specified ID or an error message if not found.
     */
    @Operation(summary = "Get a building by ID", description = "Retrieves a specific building by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Building retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BuildingDto.class))),
            @ApiResponse(responseCode = "404", description = "Building not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No building found with id - {id}\"}")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBuildingById(@PathVariable Integer id) {
        Optional<BuildingDto> building = buildingService.getBuildingById(id);

        if (building.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(building);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("{\"error\":\"No building found with id - " + id + "\"}");
    }

    /**
     * Retrieves buildings associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A list of buildings owned by the specified user or an error message if none are found.
     */
    @Operation(summary = "Get buildings by user ID", description = "Retrieves buildings associated with a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Buildings retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BuildingDto.class))),
            @ApiResponse(responseCode = "404", description = "No buildings found for the user",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No buildings found for user with id - {userId}\"}")))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getBuildingByUserId(@PathVariable Integer userId) {
        List<BuildingDto> buildings = buildingService.getBuildingsByUser(userId);

        if (buildings.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"No buildings found for user with id - " + userId + "\"}");
        }
        return ResponseEntity.ok(buildings);
    }

    /**
     * Creates a new building.
     *
     * @param buildingDto The building data to be created.
     * @return The created building or an error message.
     */
    @Operation(summary = "Create a new building", description = "Creates a new building with the provided data.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Building created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BuildingDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid building data",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"Invalid building data\"}")))
    })
    @PreAuthorize("hasAnyAuthority('System_Administrator', 'Global_Administrator')")
    @PostMapping
    public ResponseEntity<?> createBuilding(@RequestBody BuildingDto buildingDto) {
        try {
            Optional<BuildingDto> createdBuilding = buildingService.createBuilding(buildingDto);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createdBuilding);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Updates an existing building.
     *
     * @param id          The ID of the building to be updated.
     * @param buildingDto The updated building data.
     * @return The updated building or an error message if not found.
     */
    @Operation(summary = "Update a building", description = "Updates the details of an existing building.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Building updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BuildingDto.class))),
            @ApiResponse(responseCode = "404", description = "Building not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No building found with id - {id}\"}")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBuilding(@PathVariable Integer id,
                                            @RequestBody BuildingDto buildingDto) {
        try {
            Optional<BuildingDto> updatedBuilding = buildingService.updateBuilding(id, buildingDto);
            return ResponseEntity.ok(updatedBuilding);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Updates the condition of an existing building.
     *
     * @param id          The ID of the building to update.
     * @param newCondition The new condition for the building.
     * @return The updated building or an error message if not found.
     */
    @Operation(summary = "Update building condition", description = "Updates the condition of a building by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Building condition updated successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = BuildingDto.class))),
            @ApiResponse(responseCode = "404", description = "Building not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No building found with id - {id}\"}")))
    })
    @PatchMapping("/condition")
    public ResponseEntity<?> updateBuildingCondition(@RequestParam Integer id,
                                                     @RequestParam String newCondition) {
        try {
            Optional<BuildingDto> updatedBuilding = buildingService.updateBuildingCondition(id, newCondition);
            return ResponseEntity.ok(updatedBuilding);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    /**
     * Deletes a building.
     *
     * @param id The ID of the building to be deleted.
     * @return No content if the deletion is successful or an error message if not found.
     */
    @Operation(summary = "Delete a building", description = "Deletes a building by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Building deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Building not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"error\":\"No building found with id - {id}\"}")))
    })
    @PreAuthorize("hasAuthority('Global_Administrator')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBuilding(@PathVariable Integer id) {
        try {
            buildingService.deleteBuilding(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
