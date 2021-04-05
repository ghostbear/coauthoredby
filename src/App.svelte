<script>
	let coAuthors = [
		{ username: "ghost" }
	]

	function add() {
		coAuthors = coAuthors.concat([{ username: "" }])
	}

	function reset() {
		coAuthors = [{ username: "" }]
	}

	function remove(coAuthor) {
		coAuthors = coAuthors.filter(author => author !== coAuthor)
	}

	function copyToClipboard() {
		navigator.clipboard.writeText(snippet)
	}

	$: commitMessage = coAuthors.filter(author => author.username.length > 0).map(author => `Co-authored-by: ${author.username} &lt;${author.username}@users.noreply.github.com&gt;`).join("<br>")
</script>

<main>
	<h1>Co-authored</h1>
	<p>Simple tool to give credit to users who help you with a commit on GitHub</p>
	<br>
	<b>What is the username of the co-author(s)?</b>
	<div class="co-authors">
		{#each coAuthors as coAuthor}
			<div class="co-author">
				<input placeholder="username" bind:value={coAuthor.username}>
				<button on:click={remove(coAuthor)}>
					Remove
				</button>
			</div>
		{/each}
	</div>

	<div>
		<button on:click={add}>
			Add new
		</button>
		<button on:click={reset}>
			Reset
		</button>
	</div>

	{#if commitMessage}
		<code id="co-author" on:click={copyToClipboard} contenteditable="true" bind:innerHTML={commitMessage}></code>
	{/if}
</main>

<style>
	main {
		text-align: center;
		padding: 1em;
		max-width: 240px;
		margin: 0 auto;
	}

	h1 {
		color: #ff3e00;
		text-transform: uppercase;
		font-size: 4em;
		font-weight: 100;
	}

	@media (min-width: 640px) {
		main {
			max-width: none;
		}
	}
</style>