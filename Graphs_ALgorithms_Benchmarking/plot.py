import matplotlib.pyplot as plt
import numpy as np

plt.style.use('seaborn-v0_8-whitegrid')
fig, axes = plt.subplots(1, 3, figsize=(20, 6.5))
fig.suptitle('Graph Algorithms Benchmarking Results', fontsize=16, fontweight='bold')

# ── Plot 1: Prim vs Kruskal (Log Scale) ───────────────────────────────────────
ax1 = axes[0]
graphs = ['Sparse', 'Dense', 'Complete']
prim_mean    = [2.35, 68.55, 79.80]
kruskal_mean = [3.15, 500.20, 1780.15]
prim_std     = [0.57, 0.74, 1.44]
kruskal_std  = [1.46, 11.68, 52.07]

x = np.arange(len(graphs))
w = 0.35
bars1 = ax1.bar(x - w/2, prim_mean,    w, label='Prim',    color='steelblue',  yerr=prim_std,    capsize=4, error_kw={'elinewidth':1.2})
bars2 = ax1.bar(x + w/2, kruskal_mean, w, label='Kruskal', color='darkorange', yerr=kruskal_std, capsize=4, error_kw={'elinewidth':1.2})

ax1.set_yscale('log') # Use log scale to show small values clearly
ax1.set_title("Prim's vs Kruskal's MST", fontsize=13, fontweight='bold', pad=10)
ax1.set_xlabel('Graph Type', labelpad=8)
ax1.set_ylabel('Mean Time (ms)', labelpad=8)
ax1.set_xticks(x)
ax1.set_xticklabels(graphs)
ax1.legend(loc='upper left')
ax1.set_ylim(0.5, 5000) # Set logical log limits

def auto_label_log(ax, bars, std_devs):
    for bar, std in zip(bars, std_devs):
        height = bar.get_height()
        top_of_bar = height + std
        # On a log scale, spacing must be multiplicative, not additive
        ax.text(bar.get_x() + bar.get_width()/2, top_of_bar * 1.2,
                f'{height:.1f}', ha='center', va='bottom', fontsize=9)

auto_label_log(ax1, bars1, prim_std)
auto_label_p1 = auto_label_log(ax1, bars2, kruskal_std)

# ── Plot 2: Dijkstra across densities (Log Scale) ────────────────────────────
ax2 = axes[1]
graphs2 = ['Sparse', 'Dense', 'Complete', 'DAG']
dijkstra_mean = [2.05, 223.60, 390.85, 0.45]
dijkstra_std  = [0.22, 8.01,   4.82,   0.50]

colors = ['#4CAF50', '#2196F3', '#9C27B0', '#FF5722']
bars3 = ax2.bar(graphs2, dijkstra_mean, color=colors, yerr=dijkstra_std, capsize=4, error_kw={'elinewidth':1.2})
ax2.set_yscale('log')
ax2.set_title("Dijkstra's SSSP Across Graph Types", fontsize=13, fontweight='bold', pad=10)
ax2.set_xlabel('Graph Type', labelpad=8)
ax2.set_ylabel('Mean Time (ms)', labelpad=8)
ax2.set_ylim(0.1, 1000)

for bar, std in zip(bars3, dijkstra_std):
    height = bar.get_height()
    top_of_bar = height + std
    ax2.text(bar.get_x() + bar.get_width()/2, top_of_bar * 1.2,
            f'{height:.2f}', ha='center', va='bottom', fontsize=9)

# ── Plot 3: Dijkstra vs DAG Shortest Path ─────────────────────────────────────
ax3 = axes[2]
algorithms  = ["Dijkstra", "DAG Shortest Path"]
means       = [986.70, 717.75]
medians     = [980.50, 673.00]
stds        = [24.17,  139.99]

x3 = np.arange(len(algorithms))
w3 = 0.35
b1 = ax3.bar(x3 - w3/2, means,   w3, label='Mean',   color='steelblue',  yerr=stds, capsize=5, error_kw={'elinewidth':1.5})
b2 = ax3.bar(x3 + w3/2, medians, w3, label='Median', color='mediumseagreen')

ax3.set_title("Dijkstra vs DAG Shortest Path (DAG topology)", fontsize=13, fontweight='bold', pad=10)
ax3.set_xlabel('Algorithm', labelpad=8)
ax3.set_ylabel('Time (µs)', labelpad=8)
ax3.set_xticks(x3)
ax3.set_xticklabels(algorithms)
ax3.legend(loc='upper right')
ax3.set_ylim(0, max(means) * 1.25)

speedup = 986.70 / 717.75
ax3.text(0.50, 0.95, f'Mean Speedup: {speedup:.2f}x', transform=ax3.transAxes,
         ha='center', va='top', fontsize=10, color='darkred', fontweight='bold',
         bbox=dict(boxstyle='round,pad=0.4', facecolor='lightyellow', edgecolor='darkred', alpha=0.9))

for bar, std in zip(b1, stds):
    ax3.text(bar.get_x() + bar.get_width()/2, bar.get_height() + std + 15,
            f'{bar.get_height():.0f}', ha='center', va='bottom', fontsize=9)
for bar in b2:
    ax3.text(bar.get_x() + bar.get_width()/2, bar.get_height() + 15,
            f'{bar.get_height():.0f}', ha='center', va='bottom', fontsize=9)

fig.tight_layout(rect=[0, 0, 1, 0.93])
plt.savefig('benchmark_plots.png', dpi=150, bbox_inches='tight')
print("Done!")
1